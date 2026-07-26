package com.awesomeapp.f330ui

sealed class State2371_5 {
    data object Loading : State2371_5()
    data class Success(val data: String) : State2371_5()
    data class Error(val message: String) : State2371_5()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
