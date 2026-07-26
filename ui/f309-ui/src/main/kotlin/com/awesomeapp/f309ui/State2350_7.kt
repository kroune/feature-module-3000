package com.awesomeapp.f309ui

sealed class State2350_7 {
    data object Loading : State2350_7()
    data class Success(val data: String) : State2350_7()
    data class Error(val message: String) : State2350_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
