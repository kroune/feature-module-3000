package com.awesomeapp.f152ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase2193_5 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase2193_5 UseCase")
    }
}
