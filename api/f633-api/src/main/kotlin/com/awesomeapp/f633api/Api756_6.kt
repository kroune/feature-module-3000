package com.awesomeapp.f633api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api756_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api756_6 API"
    }
}
