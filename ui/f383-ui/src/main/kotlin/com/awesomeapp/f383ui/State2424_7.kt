package com.awesomeapp.f383ui

sealed class State2424_7 {
    data object Loading : State2424_7()
    data class Success(val data: String) : State2424_7()
    data class Error(val message: String) : State2424_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
