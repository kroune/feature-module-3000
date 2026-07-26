package com.awesomeapp.f192api

sealed class State315_8 {
    data object Loading : State315_8()
    data class Success(val data: String) : State315_8()
    data class Error(val message: String) : State315_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
