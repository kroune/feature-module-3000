package com.awesomeapp.f323ui

sealed class State2364_10 {
    data object Loading : State2364_10()
    data class Success(val data: String) : State2364_10()
    data class Error(val message: String) : State2364_10()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
