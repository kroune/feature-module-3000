package com.awesomeapp.f251ui

sealed class State2292_7 {
    data object Loading : State2292_7()
    data class Success(val data: String) : State2292_7()
    data class Error(val message: String) : State2292_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
