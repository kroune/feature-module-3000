package com.awesomeapp.f480ui

sealed class State2521_8 {
    data object Loading : State2521_8()
    data class Success(val data: String) : State2521_8()
    data class Error(val message: String) : State2521_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
