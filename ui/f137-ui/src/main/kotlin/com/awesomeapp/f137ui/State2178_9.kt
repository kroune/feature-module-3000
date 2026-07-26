package com.awesomeapp.f137ui

sealed class State2178_9 {
    data object Loading : State2178_9()
    data class Success(val data: String) : State2178_9()
    data class Error(val message: String) : State2178_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
