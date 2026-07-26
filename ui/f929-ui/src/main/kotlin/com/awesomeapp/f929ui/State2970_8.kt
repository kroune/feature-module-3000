package com.awesomeapp.f929ui

sealed class State2970_8 {
    data object Loading : State2970_8()
    data class Success(val data: String) : State2970_8()
    data class Error(val message: String) : State2970_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
