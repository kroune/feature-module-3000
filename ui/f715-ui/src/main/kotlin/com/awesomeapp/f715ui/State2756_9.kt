package com.awesomeapp.f715ui

sealed class State2756_9 {
    data object Loading : State2756_9()
    data class Success(val data: String) : State2756_9()
    data class Error(val message: String) : State2756_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
