package com.awesomeapp.f338ui

sealed class State2379_9 {
    data object Loading : State2379_9()
    data class Success(val data: String) : State2379_9()
    data class Error(val message: String) : State2379_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
