package com.awesomeapp.f69api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class Api192_6 constructor() {
    suspend fun fetchData(): String = withContext(Dispatchers.IO) {
        "Data from Api192_6 API"
    }
}
