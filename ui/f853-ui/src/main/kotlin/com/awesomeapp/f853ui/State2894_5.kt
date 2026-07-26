package com.awesomeapp.f853ui

sealed class State2894_5 {
    data object Loading : State2894_5()
    data class Success(val data: String) : State2894_5()
    data class Error(val message: String) : State2894_5()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
