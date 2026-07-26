package com.awesomeapp.f923ui

sealed class State2964_7 {
    data object Loading : State2964_7()
    data class Success(val data: String) : State2964_7()
    data class Error(val message: String) : State2964_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
