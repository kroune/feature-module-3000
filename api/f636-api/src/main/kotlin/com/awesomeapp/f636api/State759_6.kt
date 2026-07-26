package com.awesomeapp.f636api

sealed class State759_6 {
    data object Loading : State759_6()
    data class Success(val data: String) : State759_6()
    data class Error(val message: String) : State759_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
