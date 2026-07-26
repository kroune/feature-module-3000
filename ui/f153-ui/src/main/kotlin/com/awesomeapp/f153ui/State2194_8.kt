package com.awesomeapp.f153ui

sealed class State2194_8 {
    data object Loading : State2194_8()
    data class Success(val data: String) : State2194_8()
    data class Error(val message: String) : State2194_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
