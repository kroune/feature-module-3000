package com.awesomeapp.f934impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase2016_6 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase2016_6 UseCase")
    }
}
