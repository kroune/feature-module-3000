package com.awesomeapp.f498ui

sealed class State2539_8 {
    data object Loading : State2539_8()
    data class Success(val data: String) : State2539_8()
    data class Error(val message: String) : State2539_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
