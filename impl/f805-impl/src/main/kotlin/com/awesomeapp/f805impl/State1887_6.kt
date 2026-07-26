package com.awesomeapp.f805impl

sealed class State1887_6 {
    data object Loading : State1887_6()
    data class Success(val data: String) : State1887_6()
    data class Error(val message: String) : State1887_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
