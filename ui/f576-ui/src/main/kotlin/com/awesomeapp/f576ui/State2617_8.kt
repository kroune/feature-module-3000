package com.awesomeapp.f576ui

sealed class State2617_8 {
    data object Loading : State2617_8()
    data class Success(val data: String) : State2617_8()
    data class Error(val message: String) : State2617_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
