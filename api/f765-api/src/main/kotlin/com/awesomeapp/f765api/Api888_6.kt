package com.awesomeapp.f765api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api888_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api888_6 API"
    }
}
