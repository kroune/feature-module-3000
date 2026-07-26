package com.awesomeapp.f272ui

sealed class State2313_9 {
    data object Loading : State2313_9()
    data class Success(val data: String) : State2313_9()
    data class Error(val message: String) : State2313_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
