package com.awesomeapp.f544ui

sealed class State2585_7 {
    data object Loading : State2585_7()
    data class Success(val data: String) : State2585_7()
    data class Error(val message: String) : State2585_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
