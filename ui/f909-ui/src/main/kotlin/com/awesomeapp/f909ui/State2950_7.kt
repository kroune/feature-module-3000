package com.awesomeapp.f909ui

sealed class State2950_7 {
    data object Loading : State2950_7()
    data class Success(val data: String) : State2950_7()
    data class Error(val message: String) : State2950_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
