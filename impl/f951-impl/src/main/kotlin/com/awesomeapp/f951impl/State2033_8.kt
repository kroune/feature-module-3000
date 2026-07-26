package com.awesomeapp.f951impl

sealed class State2033_8 {
    data object Loading : State2033_8()
    data class Success(val data: String) : State2033_8()
    data class Error(val message: String) : State2033_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
