package com.awesomeapp.f647api

sealed class State770_7 {
    data object Loading : State770_7()
    data class Success(val data: String) : State770_7()
    data class Error(val message: String) : State770_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
