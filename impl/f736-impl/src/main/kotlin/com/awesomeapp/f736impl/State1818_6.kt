package com.awesomeapp.f736impl

sealed class State1818_6 {
    data object Loading : State1818_6()
    data class Success(val data: String) : State1818_6()
    data class Error(val message: String) : State1818_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
