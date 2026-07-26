package com.awesomeapp.f785ui

sealed class State2826_9 {
    data object Loading : State2826_9()
    data class Success(val data: String) : State2826_9()
    data class Error(val message: String) : State2826_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
