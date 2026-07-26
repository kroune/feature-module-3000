package com.awesomeapp.f62ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase2103_5 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase2103_5 UseCase")
    }
}
