package com.awesomeapp.f249ui

sealed class State2290_7 {
    data object Loading : State2290_7()
    data class Success(val data: String) : State2290_7()
    data class Error(val message: String) : State2290_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
