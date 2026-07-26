package com.awesomeapp.f101ui

sealed class State2142_9 {
    data object Loading : State2142_9()
    data class Success(val data: String) : State2142_9()
    data class Error(val message: String) : State2142_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
