package com.awesomeapp.f605api

sealed class State728_7 {
    data object Loading : State728_7()
    data class Success(val data: String) : State728_7()
    data class Error(val message: String) : State728_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
