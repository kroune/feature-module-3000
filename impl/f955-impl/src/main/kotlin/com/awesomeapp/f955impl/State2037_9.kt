package com.awesomeapp.f955impl

sealed class State2037_9 {
    data object Loading : State2037_9()
    data class Success(val data: String) : State2037_9()
    data class Error(val message: String) : State2037_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
