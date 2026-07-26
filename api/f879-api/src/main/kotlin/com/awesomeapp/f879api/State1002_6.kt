package com.awesomeapp.f879api

sealed class State1002_6 {
    data object Loading : State1002_6()
    data class Success(val data: String) : State1002_6()
    data class Error(val message: String) : State1002_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
