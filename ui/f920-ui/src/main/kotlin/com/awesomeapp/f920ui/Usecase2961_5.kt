package com.awesomeapp.f920ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase2961_5 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase2961_5 UseCase")
    }
}
