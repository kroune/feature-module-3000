package com.awesomeapp.f797api

sealed class State920_9 {
    data object Loading : State920_9()
    data class Success(val data: String) : State920_9()
    data class Error(val message: String) : State920_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
