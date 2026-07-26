package com.awesomeapp.f705ui

sealed class State2746_5 {
    data object Loading : State2746_5()
    data class Success(val data: String) : State2746_5()
    data class Error(val message: String) : State2746_5()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
