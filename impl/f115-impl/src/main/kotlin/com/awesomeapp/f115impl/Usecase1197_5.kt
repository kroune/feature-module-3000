package com.awesomeapp.f115impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase1197_5 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase1197_5 UseCase")
    }
}
