package ru.noteon.data.model.response

enum class State {
    SUCCESS, NOT_FOUND, FAILED, UNAUTHORIZED
}

interface Response {
    val status: State
    val message: String
}