package com.awesomeapp.f698api

sealed class State821_8 {
    data object Loading : State821_8()
    data class Success(val data: String) : State821_8()
    data class Error(val message: String) : State821_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
