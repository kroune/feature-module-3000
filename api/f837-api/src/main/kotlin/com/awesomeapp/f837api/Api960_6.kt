package com.awesomeapp.f837api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api960_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api960_6 API"
    }
}
