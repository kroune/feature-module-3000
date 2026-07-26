package com.awesomeapp.f685api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api808_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api808_6 API"
    }
}
