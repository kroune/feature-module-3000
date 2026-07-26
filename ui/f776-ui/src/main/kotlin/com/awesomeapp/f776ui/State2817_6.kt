package com.awesomeapp.f776ui

sealed class State2817_6 {
    data object Loading : State2817_6()
    data class Success(val data: String) : State2817_6()
    data class Error(val message: String) : State2817_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
