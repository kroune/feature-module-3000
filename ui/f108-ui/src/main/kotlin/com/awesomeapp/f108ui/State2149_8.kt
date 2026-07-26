package com.awesomeapp.f108ui

sealed class State2149_8 {
    data object Loading : State2149_8()
    data class Success(val data: String) : State2149_8()
    data class Error(val message: String) : State2149_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
