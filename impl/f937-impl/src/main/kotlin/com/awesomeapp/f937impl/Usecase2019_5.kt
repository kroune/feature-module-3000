package com.awesomeapp.f937impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase2019_5 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase2019_5 UseCase")
    }
}
