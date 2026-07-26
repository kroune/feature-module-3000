package com.awesomeapp.f752ui

sealed class State2793_6 {
    data object Loading : State2793_6()
    data class Success(val data: String) : State2793_6()
    data class Error(val message: String) : State2793_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
