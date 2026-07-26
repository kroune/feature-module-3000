package com.awesomeapp.f862ui

sealed class State2903_8 {
    data object Loading : State2903_8()
    data class Success(val data: String) : State2903_8()
    data class Error(val message: String) : State2903_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
