package com.awesomeapp.f205impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase1287_5 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase1287_5 UseCase")
    }
}
