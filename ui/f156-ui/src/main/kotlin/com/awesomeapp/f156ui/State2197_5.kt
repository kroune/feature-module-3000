package com.awesomeapp.f156ui

sealed class State2197_5 {
    data object Loading : State2197_5()
    data class Success(val data: String) : State2197_5()
    data class Error(val message: String) : State2197_5()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
