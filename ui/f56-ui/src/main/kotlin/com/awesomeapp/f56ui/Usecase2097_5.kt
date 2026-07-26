package com.awesomeapp.f56ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase2097_5 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase2097_5 UseCase")
    }
}
