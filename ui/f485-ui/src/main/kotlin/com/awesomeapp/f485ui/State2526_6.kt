package com.awesomeapp.f485ui

sealed class State2526_6 {
    data object Loading : State2526_6()
    data class Success(val data: String) : State2526_6()
    data class Error(val message: String) : State2526_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
