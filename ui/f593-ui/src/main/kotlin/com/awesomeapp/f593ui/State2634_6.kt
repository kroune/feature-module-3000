package com.awesomeapp.f593ui

sealed class State2634_6 {
    data object Loading : State2634_6()
    data class Success(val data: String) : State2634_6()
    data class Error(val message: String) : State2634_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
