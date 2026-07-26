package com.awesomeapp.f603api

sealed class State726_6 {
    data object Loading : State726_6()
    data class Success(val data: String) : State726_6()
    data class Error(val message: String) : State726_6()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
