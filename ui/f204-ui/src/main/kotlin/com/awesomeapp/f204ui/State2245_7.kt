package com.awesomeapp.f204ui

sealed class State2245_7 {
    data object Loading : State2245_7()
    data class Success(val data: String) : State2245_7()
    data class Error(val message: String) : State2245_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
