package com.awesomeapp.f629api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api752_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api752_6 API"
    }
}
