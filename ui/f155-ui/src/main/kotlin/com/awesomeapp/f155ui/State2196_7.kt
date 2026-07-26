package com.awesomeapp.f155ui

sealed class State2196_7 {
    data object Loading : State2196_7()
    data class Success(val data: String) : State2196_7()
    data class Error(val message: String) : State2196_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
