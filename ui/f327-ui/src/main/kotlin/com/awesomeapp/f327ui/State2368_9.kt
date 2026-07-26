package com.awesomeapp.f327ui

sealed class State2368_9 {
    data object Loading : State2368_9()
    data class Success(val data: String) : State2368_9()
    data class Error(val message: String) : State2368_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
