package com.awesomeapp.f936impl

sealed class State2018_8 {
    data object Loading : State2018_8()
    data class Success(val data: String) : State2018_8()
    data class Error(val message: String) : State2018_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
