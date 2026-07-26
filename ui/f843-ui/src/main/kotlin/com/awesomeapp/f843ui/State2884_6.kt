package com.awesomeapp.f843ui

sealed class State2884_6 {
    data object Loading : State2884_6()
    data class Success(val data: String) : State2884_6()
    data class Error(val message: String) : State2884_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
