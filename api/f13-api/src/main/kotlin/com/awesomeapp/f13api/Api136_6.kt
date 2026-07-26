package com.awesomeapp.f13api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api136_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api136_6 API"
    }
}
