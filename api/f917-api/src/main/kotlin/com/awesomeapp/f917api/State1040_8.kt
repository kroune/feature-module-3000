package com.awesomeapp.f917api

sealed class State1040_8 {
    data object Loading : State1040_8()
    data class Success(val data: String) : State1040_8()
    data class Error(val message: String) : State1040_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
