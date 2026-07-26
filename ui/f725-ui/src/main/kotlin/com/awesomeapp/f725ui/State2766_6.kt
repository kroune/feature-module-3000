package com.awesomeapp.f725ui

sealed class State2766_6 {
    data object Loading : State2766_6()
    data class Success(val data: String) : State2766_6()
    data class Error(val message: String) : State2766_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
