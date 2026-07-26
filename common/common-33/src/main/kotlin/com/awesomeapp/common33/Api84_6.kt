package com.awesomeapp.common33

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api84_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api84_6 API"
    }
}
