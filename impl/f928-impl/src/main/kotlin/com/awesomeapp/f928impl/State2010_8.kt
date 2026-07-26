package com.awesomeapp.f928impl

sealed class State2010_8 {
    data object Loading : State2010_8()
    data class Success(val data: String) : State2010_8()
    data class Error(val message: String) : State2010_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
