package com.awesomeapp.f829ui

sealed class State2870_10 {
    data object Loading : State2870_10()
    data class Success(val data: String) : State2870_10()
    data class Error(val message: String) : State2870_10()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
