#!/usr/bin/env python3
"""Patch shark-stripped hprof for MAT: rewrite top-level HEAP_DUMP (0x0C)
record tags as HEAP_DUMP_SEGMENT (0x1C). shark's strip-hprof flushes heap
sub-records as HEAP_DUMP record(s) yet still terminates them with
HEAP_DUMP_END (0x2C) — MAT's Pass1Parser increments the dump counter on BOTH
0x0C and 0x2C (0x0C is the legacy unterminated form), so the stripped file
looks like 2 snapshots ("Multiple heap dump snapshots detected") and MAT
refuses to index it. Segments + END is exactly 1 snapshot. One byte per
record, in place, offsets untouched."""
import os
import struct
import sys


def patch(path):
    size = os.path.getsize(path)
    with open(path, "r+b") as f:
        header_end = 0
        while f.read(1) != b"\x00":
            header_end += 1
        pos = header_end + 1 + 4 + 8  # version\0 + idsize + timestamp
        patched = 0
        while pos + 9 <= size:
            f.seek(pos)
            hdr = f.read(9)
            if len(hdr) < 9:
                break
            tag, _, length = hdr[0], hdr[1:5], struct.unpack(">i", hdr[5:9])[0]
            if length < 0 or pos + 9 + length > size:
                raise SystemExit(f"corrupt record at {pos}: tag 0x{tag:02x} len {length}")
            if tag == 0x0C:
                f.seek(pos)
                f.write(b"\x1c")
                patched += 1
            pos += 9 + length
        print(f"patched {patched} HEAP_DUMP record(s) in {path}")


if __name__ == "__main__":
    patch(sys.argv[1])
