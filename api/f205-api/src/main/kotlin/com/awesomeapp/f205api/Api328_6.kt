package com.awesomeapp.f205api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api328_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api328_6 API"
    }
}
