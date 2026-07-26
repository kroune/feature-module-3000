package com.awesomeapp.f837ui

sealed class State2878_8 {
    data object Loading : State2878_8()
    data class Success(val data: String) : State2878_8()
    data class Error(val message: String) : State2878_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
