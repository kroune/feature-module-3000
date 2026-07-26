package com.awesomeapp.f1impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase1083_5 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase1083_5 UseCase")
    }
}
