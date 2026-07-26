package com.awesomeapp.f825ui

sealed class State2866_8 {
    data object Loading : State2866_8()
    data class Success(val data: String) : State2866_8()
    data class Error(val message: String) : State2866_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
