package com.awesomeapp.f304ui

sealed class State2345_10 {
    data object Loading : State2345_10()
    data class Success(val data: String) : State2345_10()
    data class Error(val message: String) : State2345_10()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
