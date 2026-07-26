package com.awesomeapp.f945impl

sealed class State2027_8 {
    data object Loading : State2027_8()
    data class Success(val data: String) : State2027_8()
    data class Error(val message: String) : State2027_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
