package com.awesomeapp.foundation47

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api48_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api48_6 API"
    }
}
