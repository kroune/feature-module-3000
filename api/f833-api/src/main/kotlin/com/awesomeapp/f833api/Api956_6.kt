package com.awesomeapp.f833api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api956_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api956_6 API"
    }
}
