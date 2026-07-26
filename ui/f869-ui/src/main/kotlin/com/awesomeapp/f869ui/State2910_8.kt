package com.awesomeapp.f869ui

sealed class State2910_8 {
    data object Loading : State2910_8()
    data class Success(val data: String) : State2910_8()
    data class Error(val message: String) : State2910_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
