package com.awesomeapp.f733api

sealed class State856_7 {
    data object Loading : State856_7()
    data class Success(val data: String) : State856_7()
    data class Error(val message: String) : State856_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
