package com.awesomeapp.f635ui

sealed class State2676_10 {
    data object Loading : State2676_10()
    data class Success(val data: String) : State2676_10()
    data class Error(val message: String) : State2676_10()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
