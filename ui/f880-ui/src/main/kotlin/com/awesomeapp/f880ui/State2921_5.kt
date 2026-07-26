package com.awesomeapp.f880ui

sealed class State2921_5 {
    data object Loading : State2921_5()
    data class Success(val data: String) : State2921_5()
    data class Error(val message: String) : State2921_5()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
