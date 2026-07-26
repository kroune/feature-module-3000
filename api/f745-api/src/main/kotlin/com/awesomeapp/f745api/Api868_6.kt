package com.awesomeapp.f745api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api868_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api868_6 API"
    }
}
