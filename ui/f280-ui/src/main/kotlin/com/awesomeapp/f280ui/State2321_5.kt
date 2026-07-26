package com.awesomeapp.f280ui

sealed class State2321_5 {
    data object Loading : State2321_5()
    data class Success(val data: String) : State2321_5()
    data class Error(val message: String) : State2321_5()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
