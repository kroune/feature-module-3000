package com.awesomeapp.f717ui

sealed class State2758_5 {
    data object Loading : State2758_5()
    data class Success(val data: String) : State2758_5()
    data class Error(val message: String) : State2758_5()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
