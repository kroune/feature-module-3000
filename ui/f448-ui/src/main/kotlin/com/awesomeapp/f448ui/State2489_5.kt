package com.awesomeapp.f448ui

sealed class State2489_5 {
    data object Loading : State2489_5()
    data class Success(val data: String) : State2489_5()
    data class Error(val message: String) : State2489_5()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
