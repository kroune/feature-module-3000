package com.awesomeapp.f801ui

sealed class State2842_8 {
    data object Loading : State2842_8()
    data class Success(val data: String) : State2842_8()
    data class Error(val message: String) : State2842_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
