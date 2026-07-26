package com.awesomeapp.f110ui

sealed class State2151_6 {
    data object Loading : State2151_6()
    data class Success(val data: String) : State2151_6()
    data class Error(val message: String) : State2151_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
