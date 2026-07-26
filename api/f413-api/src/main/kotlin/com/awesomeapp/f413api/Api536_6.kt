package com.awesomeapp.f413api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api536_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api536_6 API"
    }
}
