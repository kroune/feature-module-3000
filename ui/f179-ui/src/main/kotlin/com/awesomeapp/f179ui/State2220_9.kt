package com.awesomeapp.f179ui

sealed class State2220_9 {
    data object Loading : State2220_9()
    data class Success(val data: String) : State2220_9()
    data class Error(val message: String) : State2220_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
