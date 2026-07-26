package com.awesomeapp.f545ui

sealed class State2586_9 {
    data object Loading : State2586_9()
    data class Success(val data: String) : State2586_9()
    data class Error(val message: String) : State2586_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
