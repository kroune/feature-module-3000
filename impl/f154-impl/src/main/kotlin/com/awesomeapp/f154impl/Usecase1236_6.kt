package com.awesomeapp.f154impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase1236_6 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase1236_6 UseCase")
    }
}
