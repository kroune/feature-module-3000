package com.awesomeapp.f131ui

sealed class State2172_7 {
    data object Loading : State2172_7()
    data class Success(val data: String) : State2172_7()
    data class Error(val message: String) : State2172_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
