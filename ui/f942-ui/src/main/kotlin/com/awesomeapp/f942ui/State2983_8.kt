package com.awesomeapp.f942ui

sealed class State2983_8 {
    data object Loading : State2983_8()
    data class Success(val data: String) : State2983_8()
    data class Error(val message: String) : State2983_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
