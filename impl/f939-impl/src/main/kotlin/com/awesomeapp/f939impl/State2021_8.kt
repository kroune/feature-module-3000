package com.awesomeapp.f939impl

sealed class State2021_8 {
    data object Loading : State2021_8()
    data class Success(val data: String) : State2021_8()
    data class Error(val message: String) : State2021_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
