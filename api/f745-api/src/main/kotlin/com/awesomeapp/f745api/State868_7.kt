package com.awesomeapp.f745api

sealed class State868_7 {
    data object Loading : State868_7()
    data class Success(val data: String) : State868_7()
    data class Error(val message: String) : State868_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
