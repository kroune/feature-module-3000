package com.awesomeapp.f701api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api824_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api824_6 API"
    }
}
