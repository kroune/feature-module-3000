package com.awesomeapp.f147ui

sealed class State2188_6 {
    data object Loading : State2188_6()
    data class Success(val data: String) : State2188_6()
    data class Error(val message: String) : State2188_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
