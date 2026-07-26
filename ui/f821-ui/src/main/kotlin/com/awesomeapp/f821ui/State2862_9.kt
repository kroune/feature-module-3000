package com.awesomeapp.f821ui

sealed class State2862_9 {
    data object Loading : State2862_9()
    data class Success(val data: String) : State2862_9()
    data class Error(val message: String) : State2862_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
