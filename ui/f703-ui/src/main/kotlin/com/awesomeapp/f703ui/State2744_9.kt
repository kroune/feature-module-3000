package com.awesomeapp.f703ui

sealed class State2744_9 {
    data object Loading : State2744_9()
    data class Success(val data: String) : State2744_9()
    data class Error(val message: String) : State2744_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
