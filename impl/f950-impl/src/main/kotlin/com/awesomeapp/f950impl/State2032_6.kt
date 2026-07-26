package com.awesomeapp.f950impl

sealed class State2032_6 {
    data object Loading : State2032_6()
    data class Success(val data: String) : State2032_6()
    data class Error(val message: String) : State2032_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
