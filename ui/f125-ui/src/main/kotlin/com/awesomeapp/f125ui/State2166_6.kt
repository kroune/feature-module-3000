package com.awesomeapp.f125ui

sealed class State2166_6 {
    data object Loading : State2166_6()
    data class Success(val data: String) : State2166_6()
    data class Error(val message: String) : State2166_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
