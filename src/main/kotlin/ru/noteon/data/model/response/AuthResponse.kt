package ru.noteon.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    override val status: State,
    override val message: String,
    val accessToken: String? = null,
    val refreshToken: String? = null
): Response {
    companion object {
        fun failed(message: String) = AuthResponse(
            State.FAILED,
            message
        )

        fun unauthorized(message: String) = AuthResponse(
            State.UNAUTHORIZED,
            message
        )

        fun success(accessToken: String, refreshToken: String, message: String) = AuthResponse(
            State.SUCCESS,
            message,
            accessToken,
            refreshToken,
        )
    }
}