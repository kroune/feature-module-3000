package com.awesomeapp.f547ui

sealed class State2588_6 {
    data object Loading : State2588_6()
    data class Success(val data: String) : State2588_6()
    data class Error(val message: String) : State2588_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
