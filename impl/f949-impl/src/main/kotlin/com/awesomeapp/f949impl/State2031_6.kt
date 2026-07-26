package com.awesomeapp.f949impl

sealed class State2031_6 {
    data object Loading : State2031_6()
    data class Success(val data: String) : State2031_6()
    data class Error(val message: String) : State2031_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
