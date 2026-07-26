package com.awesomeapp.f50ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase2091_5 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase2091_5 UseCase")
    }
}
