package com.awesomeapp.f12api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase135_7 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase135_7 UseCase")
    }
}
