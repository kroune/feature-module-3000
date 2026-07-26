package com.awesomeapp.f648ui

sealed class State2689_5 {
    data object Loading : State2689_5()
    data class Success(val data: String) : State2689_5()
    data class Error(val message: String) : State2689_5()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
