package com.awesomeapp.f80ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase2121_5 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase2121_5 UseCase")
    }
}
