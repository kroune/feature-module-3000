package com.awesomeapp.common13

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api64_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api64_6 API"
    }
}
