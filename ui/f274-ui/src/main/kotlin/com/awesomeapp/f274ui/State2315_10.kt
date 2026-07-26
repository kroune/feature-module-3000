package com.awesomeapp.f274ui

sealed class State2315_10 {
    data object Loading : State2315_10()
    data class Success(val data: String) : State2315_10()
    data class Error(val message: String) : State2315_10()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
