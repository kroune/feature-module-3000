package com.awesomeapp.f697api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api820_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api820_6 API"
    }
}
