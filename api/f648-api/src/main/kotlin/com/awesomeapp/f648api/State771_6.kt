package com.awesomeapp.f648api

sealed class State771_6 {
    data object Loading : State771_6()
    data class Success(val data: String) : State771_6()
    data class Error(val message: String) : State771_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
