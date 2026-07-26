package com.awesomeapp.core6

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase93_5 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase93_5 UseCase")
    }
}
