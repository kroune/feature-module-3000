package com.awesomeapp.f6api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase129_5 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase129_5 UseCase")
    }
}
