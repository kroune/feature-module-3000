package com.awesomeapp.f99api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class Usecase222_5 constructor() {
    operator fun invoke(): Flow<String> = flow {
        emit("Data from Usecase222_5 UseCase")
    }
}
