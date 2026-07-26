package com.awesomeapp.f29api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api152_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api152_6 API"
    }
}
