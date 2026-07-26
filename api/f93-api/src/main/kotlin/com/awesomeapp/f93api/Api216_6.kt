package com.awesomeapp.f93api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api216_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api216_6 API"
    }
}
