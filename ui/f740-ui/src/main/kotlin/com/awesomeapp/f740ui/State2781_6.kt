package com.awesomeapp.f740ui

sealed class State2781_6 {
    data object Loading : State2781_6()
    data class Success(val data: String) : State2781_6()
    data class Error(val message: String) : State2781_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
