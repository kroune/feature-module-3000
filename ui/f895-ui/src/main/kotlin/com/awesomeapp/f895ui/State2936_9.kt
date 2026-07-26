package com.awesomeapp.f895ui

sealed class State2936_9 {
    data object Loading : State2936_9()
    data class Success(val data: String) : State2936_9()
    data class Error(val message: String) : State2936_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
