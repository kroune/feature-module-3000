package com.awesomeapp.f57api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase180_9 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase180_9 UseCase")
    }
}
