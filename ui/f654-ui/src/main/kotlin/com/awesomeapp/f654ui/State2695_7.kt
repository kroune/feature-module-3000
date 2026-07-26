package com.awesomeapp.f654ui

sealed class State2695_7 {
    data object Loading : State2695_7()
    data class Success(val data: String) : State2695_7()
    data class Error(val message: String) : State2695_7()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
