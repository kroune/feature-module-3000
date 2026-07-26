package com.awesomeapp.f478ui

sealed class State2519_8 {
    data object Loading : State2519_8()
    data class Success(val data: String) : State2519_8()
    data class Error(val message: String) : State2519_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
