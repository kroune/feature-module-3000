package com.awesomeapp.f927impl

sealed class State2009_5 {
    data object Loading : State2009_5()
    data class Success(val data: String) : State2009_5()
    data class Error(val message: String) : State2009_5()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
