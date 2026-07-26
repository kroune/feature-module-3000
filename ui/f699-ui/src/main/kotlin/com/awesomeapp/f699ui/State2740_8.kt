package com.awesomeapp.f699ui

sealed class State2740_8 {
    data object Loading : State2740_8()
    data class Success(val data: String) : State2740_8()
    data class Error(val message: String) : State2740_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
