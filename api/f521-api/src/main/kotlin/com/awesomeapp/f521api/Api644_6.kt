package com.awesomeapp.f521api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api644_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api644_6 API"
    }
}
