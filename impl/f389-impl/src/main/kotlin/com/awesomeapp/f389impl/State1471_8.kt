package com.awesomeapp.f389impl

sealed class State1471_8 {
    data object Loading : State1471_8()
    data class Success(val data: String) : State1471_8()
    data class Error(val message: String) : State1471_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
