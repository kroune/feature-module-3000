package com.awesomeapp.f65ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase2106_5 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase2106_5 UseCase")
    }
}
