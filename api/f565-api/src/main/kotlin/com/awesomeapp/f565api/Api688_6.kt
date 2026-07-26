package com.awesomeapp.f565api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api688_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api688_6 API"
    }
}
