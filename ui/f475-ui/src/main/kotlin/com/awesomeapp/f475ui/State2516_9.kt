package com.awesomeapp.f475ui

sealed class State2516_9 {
    data object Loading : State2516_9()
    data class Success(val data: String) : State2516_9()
    data class Error(val message: String) : State2516_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
