package com.awesomeapp.f173ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase2214_5 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase2214_5 UseCase")
    }
}
