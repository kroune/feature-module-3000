package com.awesomeapp.f708api

sealed class State831_6 {
    data object Loading : State831_6()
    data class Success(val data: String) : State831_6()
    data class Error(val message: String) : State831_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
