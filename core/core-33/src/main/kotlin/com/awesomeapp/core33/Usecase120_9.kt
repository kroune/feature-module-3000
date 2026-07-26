package com.awesomeapp.core33

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase120_9 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase120_9 UseCase")
    }
}
