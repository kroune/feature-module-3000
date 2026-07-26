package com.awesomeapp.f516ui

sealed class State2557_5 {
    data object Loading : State2557_5()
    data class Success(val data: String) : State2557_5()
    data class Error(val message: String) : State2557_5()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
