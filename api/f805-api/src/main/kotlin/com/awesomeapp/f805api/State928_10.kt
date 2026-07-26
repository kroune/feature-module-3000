package com.awesomeapp.f805api

sealed class State928_10 {
    data object Loading : State928_10()
    data class Success(val data: String) : State928_10()
    data class Error(val message: String) : State928_10()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
