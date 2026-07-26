package com.awesomeapp.f627ui

sealed class State2668_6 {
    data object Loading : State2668_6()
    data class Success(val data: String) : State2668_6()
    data class Error(val message: String) : State2668_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
