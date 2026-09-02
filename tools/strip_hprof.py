#!/usr/bin/env python3
"""Streaming hprof stripper: copies a heap dump while zeroing all
PRIMITIVE_ARRAY_DUMP (0x23) contents IN PLACE (same byte count), producing a
much more compressible file that stays offset-identical to the original —
so MAT parses it exactly like the full dump and prebuilt indexes remain
valid for both variants.

Why not shark-cli strip-hprof: it buffers each continuous heap-dump run in
memory (18 GB dumps would OOM it) and writes the record length as a signed
int32 (overflows past 2 GiB, corrupting the file), and its HEAP_DUMP +
HEAP_DUMP_END combination is counted as two snapshots by MAT.

Usage: strip_hprof.py <input.hprof> <output.hprof>
"""
import os
import struct
import sys

CHUNK = 8 << 20
ZEROS = b"\x00" * CHUNK

# value type -> size in bytes (2 = object reference, idsize-dependent)
BASE_VALUE_SIZE = {4: 1, 5: 2, 6: 4, 7: 8, 8: 1, 9: 2, 10: 4, 11: 8}

# fixed-size root sub-records: tag -> body size beyond the tag byte
# (id fields are idsize each; JNI/Java frame roots carry two u4s, etc.)
ROOT_FIXED = {
    0x01: ("ii",), 0x02: ("i44",), 0x03: ("i44",), 0x04: ("i4",),
    0x05: ("i",), 0x06: ("i4",), 0x07: ("i",), 0x08: ("i44",),
    0x89: ("i",), 0x8A: ("i",), 0x8B: ("i",), 0x8C: ("i",),
    0x8D: ("i",), 0x8E: ("i44",), 0x90: ("i",), 0xFE: ("4i",),
    0xFF: ("i",),
}


class Copier:
    def __init__(self, src, dst, idsize):
        self.src, self.dst, self.idsize = src, dst, idsize
        self.arrays = 0
        self.array_bytes = 0

    def copy(self, n):
        while n > 0:
            buf = self.src.read(min(n, CHUNK))
            if not buf:
                raise EOFError("truncated record body")
            self.dst.write(buf)
            n -= len(buf)

    def skip_zero(self, n):
        """Replace n input bytes with n zero bytes in the output."""
        self.array_bytes += n
        while n > 0:
            w = min(n, CHUNK)
            if len(self.src.read(w)) != w:
                raise EOFError("truncated array payload")
            self.dst.write(ZEROS[:w])
            n -= w

    def read(self, n):
        buf = self.src.read(n)
        if len(buf) != n:
            raise EOFError("truncated sub-record")
        return buf

    def value_size(self, vtype):
        return self.idsize if vtype == 2 else BASE_VALUE_SIZE[vtype]

    def class_dump(self):
        c = self
        c.copy(c.idsize + 4 + 6 * c.idsize + 4)          # ids + serials + instance size
        pool = struct.unpack(">H", c.read(2))[0]
        c.dst.write(struct.pack(">H", pool))
        for _ in range(pool):                            # u2 index, u1 type, value
            head = c.read(3)
            c.dst.write(head)
            c.copy(c.value_size(head[2]))
        for what in ("static", "instance"):
            count = struct.unpack(">H", c.read(2))[0]
            c.dst.write(struct.pack(">H", count))
            for _ in range(count):                       # id name, u1 type, [value]
                head = c.read(c.idsize + 1)
                c.dst.write(head)
                if what == "static":
                    c.copy(c.value_size(head[c.idsize]))

    def heap_record_body(self, length):
        end = self.src.tell() + length
        while self.src.tell() < end:
            tag = self.src.read(1)
            if not tag:
                break
            t = tag[0]
            self.dst.write(tag)
            if t == 0x23:                                # PRIMITIVE_ARRAY_DUMP
                head = self.read(self.idsize + 9)        # id, u4 serial, u4 count, u1 type
                self.dst.write(head)
                count = struct.unpack(">I", head[self.idsize + 4:self.idsize + 8])[0]
                esize = BASE_VALUE_SIZE.get(head[self.idsize + 8])
                if esize is None:                        # object array masquerading: id-typed
                    esize = self.idsize
                n = count * esize
                if self.src.tell() + n > end:            # corrupt: copy rest verbatim
                    self.copy(end - self.src.tell())
                    break
                self.skip_zero(n)
                self.arrays += 1
            elif t == 0x21:                              # INSTANCE_DUMP
                head = self.read(self.idsize + 4 + self.idsize + 4)
                self.dst.write(head)
                self.copy(struct.unpack(">I", head[-4:])[0])
            elif t == 0x22:                              # OBJECT_ARRAY_DUMP
                head = self.read(self.idsize + 4 + 4 + self.idsize)
                self.dst.write(head)
                count = struct.unpack(">I", head[self.idsize + 4:self.idsize + 8])[0]
                self.copy(count * self.idsize)
            elif t == 0x20:                              # CLASS_DUMP
                self.class_dump()
            elif t in ROOT_FIXED:                        # fixed-size roots / heap info
                size = sum(self.idsize if p == "i" else int(p)
                           for p in ROOT_FIXED[t][0])
                if self.src.tell() + size > end:
                    self.copy(end - self.src.tell())
                    break
                self.copy(size)
            else:                                        # unknown: bail to verbatim copy
                self.copy(end - self.src.tell())
                break

    def run(self):
        header = b""
        while True:                                      # version string up to \0
            ch = self.src.read(1)
            if not ch:
                raise SystemExit("not an hprof file")
            header += ch
            if ch == b"\x00":
                break
        hdr_rest = self.read(4 + 8)
        self.idsize = struct.unpack(">i", hdr_rest[:4])[0]
        self.dst.write(header + hdr_rest)
        total = os.fstat(self.src.fileno()).st_size
        while self.src.tell() < total:
            head = self.src.read(9)
            if len(head) < 9:
                break                                    # truncated tail: stop cleanly
            self.dst.write(head)
            tag, length = head[0], struct.unpack(">i", head[5:9])[0]
            if tag in (0x0C, 0x1C):
                self.heap_record_body(length)
            else:
                self.copy(length)


def main():
    src_path, dst_path = sys.argv[1], sys.argv[2]
    with open(src_path, "rb", buffering=CHUNK) as src, \
         open(dst_path, "wb", buffering=CHUNK) as dst:
        c = Copier(src, dst, 8)
        c.run()
    print(f"zeroed {c.arrays} primitive arrays "
          f"({c.array_bytes / (1 << 30):.2f} GiB of payload) -> {dst_path}")


if __name__ == "__main__":
    main()
