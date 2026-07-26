package com.awesomeapp.f928ui

sealed class State2969_8 {
    data object Loading : State2969_8()
    data class Success(val data: String) : State2969_8()
    data class Error(val message: String) : State2969_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
