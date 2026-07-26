package com.awesomeapp.f322api

sealed class State445_7 {
    data object Loading : State445_7()
    data class Success(val data: String) : State445_7()
    data class Error(val message: String) : State445_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
