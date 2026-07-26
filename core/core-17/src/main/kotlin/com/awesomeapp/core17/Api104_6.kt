package com.awesomeapp.core17

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api104_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api104_6 API"
    }
}
