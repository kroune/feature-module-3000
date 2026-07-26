package com.awesomeapp.f293ui

sealed class State2334_9 {
    data object Loading : State2334_9()
    data class Success(val data: String) : State2334_9()
    data class Error(val message: String) : State2334_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
