package com.awesomeapp.f934impl

sealed class State2016_7 {
    data object Loading : State2016_7()
    data class Success(val data: String) : State2016_7()
    data class Error(val message: String) : State2016_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
