package com.awesomeapp.f599api

sealed class State722_5 {
    data object Loading : State722_5()
    data class Success(val data: String) : State722_5()
    data class Error(val message: String) : State722_5()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
