package com.awesomeapp.f621api

sealed class State744_8 {
    data object Loading : State744_8()
    data class Success(val data: String) : State744_8()
    data class Error(val message: String) : State744_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
