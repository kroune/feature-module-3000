package com.awesomeapp.f559api

sealed class State682_5 {
    data object Loading : State682_5()
    data class Success(val data: String) : State682_5()
    data class Error(val message: String) : State682_5()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
