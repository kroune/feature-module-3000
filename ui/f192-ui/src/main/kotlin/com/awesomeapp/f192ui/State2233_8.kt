package com.awesomeapp.f192ui

sealed class State2233_8 {
    data object Loading : State2233_8()
    data class Success(val data: String) : State2233_8()
    data class Error(val message: String) : State2233_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
