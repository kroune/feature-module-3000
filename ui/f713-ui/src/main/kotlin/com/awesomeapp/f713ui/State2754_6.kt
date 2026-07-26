package com.awesomeapp.f713ui

sealed class State2754_6 {
    data object Loading : State2754_6()
    data class Success(val data: String) : State2754_6()
    data class Error(val message: String) : State2754_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
