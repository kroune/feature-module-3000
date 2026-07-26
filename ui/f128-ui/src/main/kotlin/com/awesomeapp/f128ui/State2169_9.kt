package com.awesomeapp.f128ui

sealed class State2169_9 {
    data object Loading : State2169_9()
    data class Success(val data: String) : State2169_9()
    data class Error(val message: String) : State2169_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
