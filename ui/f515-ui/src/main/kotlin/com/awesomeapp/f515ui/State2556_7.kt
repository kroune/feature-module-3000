package com.awesomeapp.f515ui

sealed class State2556_7 {
    data object Loading : State2556_7()
    data class Success(val data: String) : State2556_7()
    data class Error(val message: String) : State2556_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
