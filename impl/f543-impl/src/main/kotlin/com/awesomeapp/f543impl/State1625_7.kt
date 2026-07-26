package com.awesomeapp.f543impl

sealed class State1625_7 {
    data object Loading : State1625_7()
    data class Success(val data: String) : State1625_7()
    data class Error(val message: String) : State1625_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
