package com.awesomeapp.f119ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase2160_8 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase2160_8 UseCase")
    }
}
