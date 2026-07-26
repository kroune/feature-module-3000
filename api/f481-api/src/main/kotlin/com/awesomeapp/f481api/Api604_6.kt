package com.awesomeapp.f481api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api604_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api604_6 API"
    }
}
