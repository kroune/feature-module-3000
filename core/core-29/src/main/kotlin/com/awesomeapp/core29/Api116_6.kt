package com.awesomeapp.core29

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api116_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api116_6 API"
    }
}
