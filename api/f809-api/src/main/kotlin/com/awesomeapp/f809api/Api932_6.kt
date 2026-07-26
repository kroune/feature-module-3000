package com.awesomeapp.f809api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api932_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api932_6 API"
    }
}
