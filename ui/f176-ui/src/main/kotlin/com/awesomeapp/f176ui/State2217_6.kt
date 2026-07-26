package com.awesomeapp.f176ui

sealed class State2217_6 {
    data object Loading : State2217_6()
    data class Success(val data: String) : State2217_6()
    data class Error(val message: String) : State2217_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
