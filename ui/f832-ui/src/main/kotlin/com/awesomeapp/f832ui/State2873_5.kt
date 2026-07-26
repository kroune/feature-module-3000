package com.awesomeapp.f832ui

sealed class State2873_5 {
    data object Loading : State2873_5()
    data class Success(val data: String) : State2873_5()
    data class Error(val message: String) : State2873_5()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
