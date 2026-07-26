package com.awesomeapp.core9

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase96_7 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase96_7 UseCase")
    }
}
