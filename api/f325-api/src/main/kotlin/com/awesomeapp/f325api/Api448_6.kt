package com.awesomeapp.f325api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api448_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api448_6 API"
    }
}
