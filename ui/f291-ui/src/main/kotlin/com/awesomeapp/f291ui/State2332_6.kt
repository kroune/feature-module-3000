package com.awesomeapp.f291ui

sealed class State2332_6 {
    data object Loading : State2332_6()
    data class Success(val data: String) : State2332_6()
    data class Error(val message: String) : State2332_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
