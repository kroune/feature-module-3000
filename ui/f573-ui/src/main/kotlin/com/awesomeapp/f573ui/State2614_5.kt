package com.awesomeapp.f573ui

sealed class State2614_5 {
    data object Loading : State2614_5()
    data class Success(val data: String) : State2614_5()
    data class Error(val message: String) : State2614_5()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
