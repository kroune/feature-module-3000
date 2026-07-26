package com.awesomeapp.f905ui

sealed class State2946_9 {
    data object Loading : State2946_9()
    data class Success(val data: String) : State2946_9()
    data class Error(val message: String) : State2946_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
