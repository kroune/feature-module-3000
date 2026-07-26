package com.awesomeapp.f288ui

sealed class State2329_8 {
    data object Loading : State2329_8()
    data class Success(val data: String) : State2329_8()
    data class Error(val message: String) : State2329_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
