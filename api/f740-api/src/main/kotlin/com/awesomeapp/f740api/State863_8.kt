package com.awesomeapp.f740api

sealed class State863_8 {
    data object Loading : State863_8()
    data class Success(val data: String) : State863_8()
    data class Error(val message: String) : State863_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
