package com.awesomeapp.f239ui

sealed class State2280_9 {
    data object Loading : State2280_9()
    data class Success(val data: String) : State2280_9()
    data class Error(val message: String) : State2280_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
