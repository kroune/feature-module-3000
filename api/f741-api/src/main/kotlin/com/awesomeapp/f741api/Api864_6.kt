package com.awesomeapp.f741api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api864_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api864_6 API"
    }
}
