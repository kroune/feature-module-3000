package com.awesomeapp.f592ui

sealed class State2633_8 {
    data object Loading : State2633_8()
    data class Success(val data: String) : State2633_8()
    data class Error(val message: String) : State2633_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
