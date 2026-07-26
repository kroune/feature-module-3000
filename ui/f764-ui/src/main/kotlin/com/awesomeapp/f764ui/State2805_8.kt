package com.awesomeapp.f764ui

sealed class State2805_8 {
    data object Loading : State2805_8()
    data class Success(val data: String) : State2805_8()
    data class Error(val message: String) : State2805_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
