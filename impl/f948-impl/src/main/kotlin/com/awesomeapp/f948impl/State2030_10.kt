package com.awesomeapp.f948impl

sealed class State2030_10 {
    data object Loading : State2030_10()
    data class Success(val data: String) : State2030_10()
    data class Error(val message: String) : State2030_10()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
