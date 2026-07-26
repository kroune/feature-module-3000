package com.awesomeapp.f23ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase2064_6 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase2064_6 UseCase")
    }
}
