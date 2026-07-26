package com.awesomeapp.f941impl

sealed class State2023_8 {
    data object Loading : State2023_8()
    data class Success(val data: String) : State2023_8()
    data class Error(val message: String) : State2023_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
