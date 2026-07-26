package com.awesomeapp.f409api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api532_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api532_6 API"
    }
}
