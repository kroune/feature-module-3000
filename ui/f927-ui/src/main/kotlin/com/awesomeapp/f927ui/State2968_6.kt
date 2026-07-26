package com.awesomeapp.f927ui

sealed class State2968_6 {
    data object Loading : State2968_6()
    data class Success(val data: String) : State2968_6()
    data class Error(val message: String) : State2968_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
