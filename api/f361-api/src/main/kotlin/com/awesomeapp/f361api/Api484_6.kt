package com.awesomeapp.f361api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api484_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api484_6 API"
    }
}
