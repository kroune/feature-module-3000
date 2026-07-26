package com.awesomeapp.f951ui

sealed class State2992_6 {
    data object Loading : State2992_6()
    data class Success(val data: String) : State2992_6()
    data class Error(val message: String) : State2992_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
