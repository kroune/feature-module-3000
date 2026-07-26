package com.awesomeapp.f589ui

sealed class State2630_7 {
    data object Loading : State2630_7()
    data class Success(val data: String) : State2630_7()
    data class Error(val message: String) : State2630_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
