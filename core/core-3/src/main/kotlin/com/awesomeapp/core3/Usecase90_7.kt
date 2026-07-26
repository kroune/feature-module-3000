package com.awesomeapp.core3

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase90_7 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase90_7 UseCase")
    }
}
