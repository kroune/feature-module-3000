package com.awesomeapp.f869api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api992_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api992_6 API"
    }
}
