package com.awesomeapp.f75ui

sealed class State2116_9 {
    data object Loading : State2116_9()
    data class Success(val data: String) : State2116_9()
    data class Error(val message: String) : State2116_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
