package com.awesomeapp.f641api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api764_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api764_6 API"
    }
}
