package com.awesomeapp.f131ui

sealed class State2172_10 {
    data object Loading : State2172_10()
    data class Success(val data: String) : State2172_10()
    data class Error(val message: String) : State2172_10()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
