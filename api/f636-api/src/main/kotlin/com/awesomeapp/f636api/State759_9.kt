package com.awesomeapp.f636api

sealed class State759_9 {
    data object Loading : State759_9()
    data class Success(val data: String) : State759_9()
    data class Error(val message: String) : State759_9()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
