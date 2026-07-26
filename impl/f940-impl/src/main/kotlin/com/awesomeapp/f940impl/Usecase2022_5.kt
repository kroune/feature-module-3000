package com.awesomeapp.f940impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase2022_5 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase2022_5 UseCase")
    }
}
