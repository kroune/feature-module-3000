package com.awesomeapp.f716ui

sealed class State2757_6 {
    data object Loading : State2757_6()
    data class Success(val data: String) : State2757_6()
    data class Error(val message: String) : State2757_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
