package com.awesomeapp.f405ui

sealed class State2446_8 {
    data object Loading : State2446_8()
    data class Success(val data: String) : State2446_8()
    data class Error(val message: String) : State2446_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
