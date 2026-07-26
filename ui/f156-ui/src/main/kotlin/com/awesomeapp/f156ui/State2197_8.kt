package com.awesomeapp.f156ui

sealed class State2197_8 {
    data object Loading : State2197_8()
    data class Success(val data: String) : State2197_8()
    data class Error(val message: String) : State2197_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
