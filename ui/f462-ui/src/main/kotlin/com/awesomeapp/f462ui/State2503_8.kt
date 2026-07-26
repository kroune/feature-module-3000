package com.awesomeapp.f462ui

sealed class State2503_8 {
    data object Loading : State2503_8()
    data class Success(val data: String) : State2503_8()
    data class Error(val message: String) : State2503_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
