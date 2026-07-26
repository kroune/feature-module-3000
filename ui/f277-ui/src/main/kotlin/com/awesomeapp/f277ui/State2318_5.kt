package com.awesomeapp.f277ui

sealed class State2318_5 {
    data object Loading : State2318_5()
    data class Success(val data: String) : State2318_5()
    data class Error(val message: String) : State2318_5()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
