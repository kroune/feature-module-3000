package com.awesomeapp.f931impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase2013_5 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase2013_5 UseCase")
    }
}
