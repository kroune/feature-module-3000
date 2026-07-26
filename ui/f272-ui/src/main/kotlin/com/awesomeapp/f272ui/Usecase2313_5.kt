package com.awesomeapp.f272ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase2313_5 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase2313_5 UseCase")
    }
}
