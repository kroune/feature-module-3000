package com.awesomeapp.f941ui

sealed class State2982_9 {
    data object Loading : State2982_9()
    data class Success(val data: String) : State2982_9()
    data class Error(val message: String) : State2982_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
