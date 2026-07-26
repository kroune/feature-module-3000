package com.awesomeapp.f753api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api876_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api876_6 API"
    }
}
