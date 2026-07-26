package com.awesomeapp.f575ui

sealed class State2616_7 {
    data object Loading : State2616_7()
    data class Success(val data: String) : State2616_7()
    data class Error(val message: String) : State2616_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
