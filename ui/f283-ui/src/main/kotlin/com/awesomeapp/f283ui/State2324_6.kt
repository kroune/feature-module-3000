package com.awesomeapp.f283ui

sealed class State2324_6 {
    data object Loading : State2324_6()
    data class Success(val data: String) : State2324_6()
    data class Error(val message: String) : State2324_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
