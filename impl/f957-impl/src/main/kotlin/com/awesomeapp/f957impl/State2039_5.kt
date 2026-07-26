package com.awesomeapp.f957impl

sealed class State2039_5 {
    data object Loading : State2039_5()
    data class Success(val data: String) : State2039_5()
    data class Error(val message: String) : State2039_5()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
