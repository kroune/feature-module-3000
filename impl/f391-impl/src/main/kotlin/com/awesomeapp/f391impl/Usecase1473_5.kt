package com.awesomeapp.f391impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase1473_5 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase1473_5 UseCase")
    }
}
