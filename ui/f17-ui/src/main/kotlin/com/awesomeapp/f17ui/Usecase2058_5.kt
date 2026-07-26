package com.awesomeapp.f17ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase2058_5 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase2058_5 UseCase")
    }
}
