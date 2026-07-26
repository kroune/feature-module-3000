package com.awesomeapp.f862api

sealed class State985_10 {
    data object Loading : State985_10()
    data class Success(val data: String) : State985_10()
    data class Error(val message: String) : State985_10()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
