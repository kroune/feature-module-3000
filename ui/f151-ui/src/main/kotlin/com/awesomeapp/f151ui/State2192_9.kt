package com.awesomeapp.f151ui

sealed class State2192_9 {
    data object Loading : State2192_9()
    data class Success(val data: String) : State2192_9()
    data class Error(val message: String) : State2192_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
