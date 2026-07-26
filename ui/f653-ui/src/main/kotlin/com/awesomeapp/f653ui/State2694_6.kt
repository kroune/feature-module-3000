package com.awesomeapp.f653ui

sealed class State2694_6 {
    data object Loading : State2694_6()
    data class Success(val data: String) : State2694_6()
    data class Error(val message: String) : State2694_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
