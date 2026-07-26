package com.awesomeapp.f855api

sealed class State978_9 {
    data object Loading : State978_9()
    data class Success(val data: String) : State978_9()
    data class Error(val message: String) : State978_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
