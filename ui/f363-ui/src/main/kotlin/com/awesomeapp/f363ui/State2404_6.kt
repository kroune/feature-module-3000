package com.awesomeapp.f363ui

sealed class State2404_6 {
    data object Loading : State2404_6()
    data class Success(val data: String) : State2404_6()
    data class Error(val message: String) : State2404_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
