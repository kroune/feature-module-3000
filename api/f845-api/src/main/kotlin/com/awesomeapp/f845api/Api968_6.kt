package com.awesomeapp.f845api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api968_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api968_6 API"
    }
}
