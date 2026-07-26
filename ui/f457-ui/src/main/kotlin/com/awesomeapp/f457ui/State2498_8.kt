package com.awesomeapp.f457ui

sealed class State2498_8 {
    data object Loading : State2498_8()
    data class Success(val data: String) : State2498_8()
    data class Error(val message: String) : State2498_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
