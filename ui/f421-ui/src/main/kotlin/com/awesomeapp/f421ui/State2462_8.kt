package com.awesomeapp.f421ui

sealed class State2462_8 {
    data object Loading : State2462_8()
    data class Success(val data: String) : State2462_8()
    data class Error(val message: String) : State2462_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
