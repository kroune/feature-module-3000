package com.awesomeapp.f645api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api768_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api768_6 API"
    }
}
