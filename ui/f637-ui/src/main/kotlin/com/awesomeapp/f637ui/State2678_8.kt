package com.awesomeapp.f637ui

sealed class State2678_8 {
    data object Loading : State2678_8()
    data class Success(val data: String) : State2678_8()
    data class Error(val message: String) : State2678_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
