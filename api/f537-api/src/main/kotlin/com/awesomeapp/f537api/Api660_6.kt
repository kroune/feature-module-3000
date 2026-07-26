package com.awesomeapp.f537api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api660_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api660_6 API"
    }
}
