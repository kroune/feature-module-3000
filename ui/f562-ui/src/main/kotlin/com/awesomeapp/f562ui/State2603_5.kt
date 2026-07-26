package com.awesomeapp.f562ui

sealed class State2603_5 {
    data object Loading : State2603_5()
    data class Success(val data: String) : State2603_5()
    data class Error(val message: String) : State2603_5()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
