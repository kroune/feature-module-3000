package com.awesomeapp.f33api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api156_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api156_6 API"
    }
}
