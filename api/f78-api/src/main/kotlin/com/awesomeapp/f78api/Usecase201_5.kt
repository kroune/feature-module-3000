package com.awesomeapp.f78api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase201_5 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase201_5 UseCase")
    }
}
