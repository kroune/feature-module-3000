package com.awesomeapp.f901ui

sealed class State2942_8 {
    data object Loading : State2942_8()
    data class Success(val data: String) : State2942_8()
    data class Error(val message: String) : State2942_8()

    companion object {
        fun loading() = Loading
        fun success(data: String) = Success(data)
        fun error(message: String) = Error(message)
    }
}
