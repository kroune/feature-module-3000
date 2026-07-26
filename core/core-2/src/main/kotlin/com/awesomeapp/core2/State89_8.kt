package com.awesomeapp.core2

sealed class State89_8 {
    data object Loading : State89_8()
    data class Success(val data: String) : State89_8()
    data class Error(val message: String) : State89_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
