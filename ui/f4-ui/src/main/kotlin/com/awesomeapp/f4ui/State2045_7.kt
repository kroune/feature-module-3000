package com.awesomeapp.f4ui

sealed class State2045_7 {
    data object Loading : State2045_7()
    data class Success(val data: String) : State2045_7()
    data class Error(val message: String) : State2045_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
