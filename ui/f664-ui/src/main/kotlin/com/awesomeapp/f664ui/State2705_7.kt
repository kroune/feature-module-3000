package com.awesomeapp.f664ui

sealed class State2705_7 {
    data object Loading : State2705_7()
    data class Success(val data: String) : State2705_7()
    data class Error(val message: String) : State2705_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
