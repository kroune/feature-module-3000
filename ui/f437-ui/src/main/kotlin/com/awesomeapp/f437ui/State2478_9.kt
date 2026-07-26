package com.awesomeapp.f437ui

sealed class State2478_9 {
    data object Loading : State2478_9()
    data class Success(val data: String) : State2478_9()
    data class Error(val message: String) : State2478_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
