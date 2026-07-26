package com.awesomeapp.f633ui

sealed class State2674_5 {
    data object Loading : State2674_5()
    data class Success(val data: String) : State2674_5()
    data class Error(val message: String) : State2674_5()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
