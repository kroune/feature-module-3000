package com.awesomeapp.f755ui

sealed class State2796_7 {
    data object Loading : State2796_7()
    data class Success(val data: String) : State2796_7()
    data class Error(val message: String) : State2796_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
