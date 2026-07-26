package com.awesomeapp.f61ui

sealed class State2102_8 {
    data object Loading : State2102_8()
    data class Success(val data: String) : State2102_8()
    data class Error(val message: String) : State2102_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
