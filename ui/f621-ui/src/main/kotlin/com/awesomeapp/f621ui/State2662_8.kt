package com.awesomeapp.f621ui

sealed class State2662_8 {
    data object Loading : State2662_8()
    data class Success(val data: String) : State2662_8()
    data class Error(val message: String) : State2662_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
