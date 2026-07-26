package com.awesomeapp.f832api

sealed class State955_7 {
    data object Loading : State955_7()
    data class Success(val data: String) : State955_7()
    data class Error(val message: String) : State955_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
