package com.awesomeapp.f653api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api776_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api776_6 API"
    }
}
