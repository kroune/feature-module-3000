package com.awesomeapp.f368ui

sealed class State2409_6 {
    data object Loading : State2409_6()
    data class Success(val data: String) : State2409_6()
    data class Error(val message: String) : State2409_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
