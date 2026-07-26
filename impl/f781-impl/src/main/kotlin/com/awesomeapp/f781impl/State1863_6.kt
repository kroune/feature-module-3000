package com.awesomeapp.f781impl

sealed class State1863_6 {
    data object Loading : State1863_6()
    data class Success(val data: String) : State1863_6()
    data class Error(val message: String) : State1863_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
