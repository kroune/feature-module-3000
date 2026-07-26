package com.awesomeapp.f367ui

sealed class State2408_9 {
    data object Loading : State2408_9()
    data class Success(val data: String) : State2408_9()
    data class Error(val message: String) : State2408_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
