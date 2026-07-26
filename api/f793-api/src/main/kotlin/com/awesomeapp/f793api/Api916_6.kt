package com.awesomeapp.f793api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api916_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api916_6 API"
    }
}
