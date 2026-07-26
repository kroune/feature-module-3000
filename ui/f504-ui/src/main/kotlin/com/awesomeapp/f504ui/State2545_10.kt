package com.awesomeapp.f504ui

sealed class State2545_10 {
    data object Loading : State2545_10()
    data class Success(val data: String) : State2545_10()
    data class Error(val message: String) : State2545_10()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
