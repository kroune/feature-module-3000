package com.awesomeapp.f858api

sealed class State981_9 {
    data object Loading : State981_9()
    data class Success(val data: String) : State981_9()
    data class Error(val message: String) : State981_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
