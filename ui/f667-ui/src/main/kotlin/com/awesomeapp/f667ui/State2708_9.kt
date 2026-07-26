package com.awesomeapp.f667ui

sealed class State2708_9 {
    data object Loading : State2708_9()
    data class Success(val data: String) : State2708_9()
    data class Error(val message: String) : State2708_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
