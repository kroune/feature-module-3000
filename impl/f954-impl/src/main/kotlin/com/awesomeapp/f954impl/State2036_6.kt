package com.awesomeapp.f954impl

sealed class State2036_6 {
    data object Loading : State2036_6()
    data class Success(val data: String) : State2036_6()
    data class Error(val message: String) : State2036_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
