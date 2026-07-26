package com.awesomeapp.f925impl

sealed class State2007_9 {
    data object Loading : State2007_9()
    data class Success(val data: String) : State2007_9()
    data class Error(val message: String) : State2007_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
