package com.awesomeapp.f760ui

sealed class State2801_8 {
    data object Loading : State2801_8()
    data class Success(val data: String) : State2801_8()
    data class Error(val message: String) : State2801_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
