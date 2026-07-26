package com.awesomeapp.f20ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase2061_5 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase2061_5 UseCase")
    }
}
