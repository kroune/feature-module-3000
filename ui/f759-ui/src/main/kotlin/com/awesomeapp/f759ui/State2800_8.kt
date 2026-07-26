package com.awesomeapp.f759ui

sealed class State2800_8 {
    data object Loading : State2800_8()
    data class Success(val data: String) : State2800_8()
    data class Error(val message: String) : State2800_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
