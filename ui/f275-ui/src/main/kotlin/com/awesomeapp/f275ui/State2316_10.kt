package com.awesomeapp.f275ui

sealed class State2316_10 {
    data object Loading : State2316_10()
    data class Success(val data: String) : State2316_10()
    data class Error(val message: String) : State2316_10()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
