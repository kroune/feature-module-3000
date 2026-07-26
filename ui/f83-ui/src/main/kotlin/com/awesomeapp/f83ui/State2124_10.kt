package com.awesomeapp.f83ui

sealed class State2124_10 {
    data object Loading : State2124_10()
    data class Success(val data: String) : State2124_10()
    data class Error(val message: String) : State2124_10()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
