package com.awesomeapp.f20ui

sealed class State2061_9 {
    data object Loading : State2061_9()
    data class Success(val data: String) : State2061_9()
    data class Error(val message: String) : State2061_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
