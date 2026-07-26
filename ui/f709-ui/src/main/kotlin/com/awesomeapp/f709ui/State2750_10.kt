package com.awesomeapp.f709ui

sealed class State2750_10 {
    data object Loading : State2750_10()
    data class Success(val data: String) : State2750_10()
    data class Error(val message: String) : State2750_10()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
