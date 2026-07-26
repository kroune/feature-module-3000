package com.awesomeapp.f157api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api280_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api280_6 API"
    }
}
