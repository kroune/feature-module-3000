package com.awesomeapp.f767ui

sealed class State2808_7 {
    data object Loading : State2808_7()
    data class Success(val data: String) : State2808_7()
    data class Error(val message: String) : State2808_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
