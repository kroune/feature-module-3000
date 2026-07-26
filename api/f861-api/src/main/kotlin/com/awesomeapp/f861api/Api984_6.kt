package com.awesomeapp.f861api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api984_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api984_6 API"
    }
}
