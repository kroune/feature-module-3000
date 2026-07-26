package com.awesomeapp.f304impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase1386_5 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase1386_5 UseCase")
    }
}
