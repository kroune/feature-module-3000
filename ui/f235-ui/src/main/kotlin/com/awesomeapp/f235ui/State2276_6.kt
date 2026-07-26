package com.awesomeapp.f235ui

sealed class State2276_6 {
    data object Loading : State2276_6()
    data class Success(val data: String) : State2276_6()
    data class Error(val message: String) : State2276_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
