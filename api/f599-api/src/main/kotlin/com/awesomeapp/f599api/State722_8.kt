package com.awesomeapp.f599api

sealed class State722_8 {
    data object Loading : State722_8()
    data class Success(val data: String) : State722_8()
    data class Error(val message: String) : State722_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
