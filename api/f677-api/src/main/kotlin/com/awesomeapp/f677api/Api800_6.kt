package com.awesomeapp.f677api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api800_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api800_6 API"
    }
}
