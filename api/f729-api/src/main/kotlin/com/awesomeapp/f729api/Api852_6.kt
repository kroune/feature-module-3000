package com.awesomeapp.f729api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api852_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api852_6 API"
    }
}
