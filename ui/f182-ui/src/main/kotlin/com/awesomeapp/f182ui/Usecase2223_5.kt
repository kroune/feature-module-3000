package com.awesomeapp.f182ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase2223_5 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase2223_5 UseCase")
    }
}
