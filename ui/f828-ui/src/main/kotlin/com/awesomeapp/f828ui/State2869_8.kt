package com.awesomeapp.f828ui

sealed class State2869_8 {
    data object Loading : State2869_8()
    data class Success(val data: String) : State2869_8()
    data class Error(val message: String) : State2869_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
