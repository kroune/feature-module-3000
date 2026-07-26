package com.awesomeapp.f389api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api512_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api512_6 API"
    }
}
