package com.awesomeapp.f109impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase1191_5 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase1191_5 UseCase")
    }
}
