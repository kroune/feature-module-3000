package com.awesomeapp.f210api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase333_5 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase333_5 UseCase")
    }
}
