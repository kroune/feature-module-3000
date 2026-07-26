package com.awesomeapp.f893ui

sealed class State2934_9 {
    data object Loading : State2934_9()
    data class Success(val data: String) : State2934_9()
    data class Error(val message: String) : State2934_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
