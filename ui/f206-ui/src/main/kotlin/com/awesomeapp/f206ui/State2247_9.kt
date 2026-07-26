package com.awesomeapp.f206ui

sealed class State2247_9 {
    data object Loading : State2247_9()
    data class Success(val data: String) : State2247_9()
    data class Error(val message: String) : State2247_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
