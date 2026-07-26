package com.awesomeapp.core33

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api120_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api120_6 API"
    }
}
