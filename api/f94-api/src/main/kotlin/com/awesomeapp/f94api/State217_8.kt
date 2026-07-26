package com.awesomeapp.f94api

sealed class State217_8 {
    data object Loading : State217_8()
    data class Success(val data: String) : State217_8()
    data class Error(val message: String) : State217_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
