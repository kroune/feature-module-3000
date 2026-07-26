package com.awesomeapp.f493ui

sealed class State2534_5 {
    data object Loading : State2534_5()
    data class Success(val data: String) : State2534_5()
    data class Error(val message: String) : State2534_5()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
