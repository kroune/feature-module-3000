package com.awesomeapp.f345ui

sealed class State2386_5 {
    data object Loading : State2386_5()
    data class Success(val data: String) : State2386_5()
    data class Error(val message: String) : State2386_5()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
