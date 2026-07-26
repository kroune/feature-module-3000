package com.awesomeapp.f323impl

sealed class State1405_7 {
    data object Loading : State1405_7()
    data class Success(val data: String) : State1405_7()
    data class Error(val message: String) : State1405_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
