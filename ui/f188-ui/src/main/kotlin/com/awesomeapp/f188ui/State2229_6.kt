package com.awesomeapp.f188ui

sealed class State2229_6 {
    data object Loading : State2229_6()
    data class Success(val data: String) : State2229_6()
    data class Error(val message: String) : State2229_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
