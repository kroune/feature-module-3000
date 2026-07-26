package com.awesomeapp.f364ui

sealed class State2405_10 {
    data object Loading : State2405_10()
    data class Success(val data: String) : State2405_10()
    data class Error(val message: String) : State2405_10()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
