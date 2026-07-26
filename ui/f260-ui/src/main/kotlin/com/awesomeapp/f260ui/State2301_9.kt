package com.awesomeapp.f260ui

sealed class State2301_9 {
    data object Loading : State2301_9()
    data class Success(val data: String) : State2301_9()
    data class Error(val message: String) : State2301_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
