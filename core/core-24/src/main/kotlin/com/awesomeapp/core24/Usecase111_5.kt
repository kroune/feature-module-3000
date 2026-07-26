package com.awesomeapp.core24

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase111_5 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase111_5 UseCase")
    }
}
