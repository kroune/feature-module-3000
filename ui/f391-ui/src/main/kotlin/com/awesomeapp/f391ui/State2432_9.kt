package com.awesomeapp.f391ui

sealed class State2432_9 {
    data object Loading : State2432_9()
    data class Success(val data: String) : State2432_9()
    data class Error(val message: String) : State2432_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
