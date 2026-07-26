package com.awesomeapp.f13impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase1095_7 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase1095_7 UseCase")
    }
}
