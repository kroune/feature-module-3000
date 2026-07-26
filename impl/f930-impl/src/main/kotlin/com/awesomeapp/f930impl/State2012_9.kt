package com.awesomeapp.f930impl

sealed class State2012_9 {
    data object Loading : State2012_9()
    data class Success(val data: String) : State2012_9()
    data class Error(val message: String) : State2012_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
