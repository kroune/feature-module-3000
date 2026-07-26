package com.awesomeapp.f583ui

sealed class State2624_6 {
    data object Loading : State2624_6()
    data class Success(val data: String) : State2624_6()
    data class Error(val message: String) : State2624_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
