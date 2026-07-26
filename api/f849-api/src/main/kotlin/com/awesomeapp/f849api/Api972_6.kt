package com.awesomeapp.f849api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api972_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api972_6 API"
    }
}
