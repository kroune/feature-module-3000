package com.awesomeapp.f565impl

sealed class State1647_9 {
    data object Loading : State1647_9()
    data class Success(val data: String) : State1647_9()
    data class Error(val message: String) : State1647_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
