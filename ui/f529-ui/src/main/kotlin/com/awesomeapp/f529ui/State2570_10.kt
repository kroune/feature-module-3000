package com.awesomeapp.f529ui

sealed class State2570_10 {
    data object Loading : State2570_10()
    data class Success(val data: String) : State2570_10()
    data class Error(val message: String) : State2570_10()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
