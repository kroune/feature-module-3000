package com.awesomeapp.f620api

sealed class State743_5 {
    data object Loading : State743_5()
    data class Success(val data: String) : State743_5()
    data class Error(val message: String) : State743_5()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
