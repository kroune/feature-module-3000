package com.awesomeapp.f214ui

sealed class State2255_7 {
    data object Loading : State2255_7()
    data class Success(val data: String) : State2255_7()
    data class Error(val message: String) : State2255_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
