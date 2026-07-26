package com.awesomeapp.f505api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api628_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api628_6 API"
    }
}
