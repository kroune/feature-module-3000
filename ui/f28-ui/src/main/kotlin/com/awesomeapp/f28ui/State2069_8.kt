package com.awesomeapp.f28ui

sealed class State2069_8 {
    data object Loading : State2069_8()
    data class Success(val data: String) : State2069_8()
    data class Error(val message: String) : State2069_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
