package com.awesomeapp.f919impl

sealed class State2001_9 {
    data object Loading : State2001_9()
    data class Success(val data: String) : State2001_9()
    data class Error(val message: String) : State2001_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
