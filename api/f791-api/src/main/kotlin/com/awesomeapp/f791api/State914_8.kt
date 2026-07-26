package com.awesomeapp.f791api

sealed class State914_8 {
    data object Loading : State914_8()
    data class Success(val data: String) : State914_8()
    data class Error(val message: String) : State914_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
