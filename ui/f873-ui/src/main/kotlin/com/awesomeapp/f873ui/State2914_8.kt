package com.awesomeapp.f873ui

sealed class State2914_8 {
    data object Loading : State2914_8()
    data class Success(val data: String) : State2914_8()
    data class Error(val message: String) : State2914_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
