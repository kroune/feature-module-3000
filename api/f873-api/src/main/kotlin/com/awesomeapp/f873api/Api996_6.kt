package com.awesomeapp.f873api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api996_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api996_6 API"
    }
}
