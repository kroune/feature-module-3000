package com.awesomeapp.f659ui

sealed class State2700_9 {
    data object Loading : State2700_9()
    data class Success(val data: String) : State2700_9()
    data class Error(val message: String) : State2700_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
