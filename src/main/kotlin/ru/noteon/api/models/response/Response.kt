package ru.noteon.api.models.response

enum class State {
    SUCCESS, NOT_FOUND, FAILED, UNAUTHORIZED
}

interface Response {
    val status: State
    val message: String
}