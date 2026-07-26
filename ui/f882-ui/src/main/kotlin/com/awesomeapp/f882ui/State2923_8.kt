package com.awesomeapp.f882ui

sealed class State2923_8 {
    data object Loading : State2923_8()
    data class Success(val data: String) : State2923_8()
    data class Error(val message: String) : State2923_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
