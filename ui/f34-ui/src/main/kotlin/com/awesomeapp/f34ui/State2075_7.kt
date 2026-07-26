package com.awesomeapp.f34ui

sealed class State2075_7 {
    data object Loading : State2075_7()
    data class Success(val data: String) : State2075_7()
    data class Error(val message: String) : State2075_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
