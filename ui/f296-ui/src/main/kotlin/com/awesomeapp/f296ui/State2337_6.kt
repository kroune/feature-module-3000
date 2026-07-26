package com.awesomeapp.f296ui

sealed class State2337_6 {
    data object Loading : State2337_6()
    data class Success(val data: String) : State2337_6()
    data class Error(val message: String) : State2337_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
