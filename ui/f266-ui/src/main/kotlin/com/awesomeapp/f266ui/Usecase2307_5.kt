package com.awesomeapp.f266ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase2307_5 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase2307_5 UseCase")
    }
}
