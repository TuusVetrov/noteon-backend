package ru.noteon.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserResponse(
    override val status: State,
    override val message: String,
    val id: String? = null,
): Response {
    companion object {
        fun failed(message: String) = UpdateUserResponse(
            State.FAILED,
            message
        )

        fun unauthorized(message: String) = UpdateUserResponse(
            State.UNAUTHORIZED,
            message
        )

        fun notFound(message: String) = UpdateUserResponse(
            State.NOT_FOUND,
            message
        )
        fun success(id: String) = UpdateUserResponse(
            State.SUCCESS,
            "Task successful",
            id
        )
    }
}

@Serializable
data class UserResponse(
    override val status: State,
    override val message: String,
    val id: String? = null,
    val username: String? = null,
    val email: String? = null
): Response {
    companion object {
        fun failed(message: String) = UserResponse(
            State.FAILED,
            message
        )

        fun unauthorized(message: String) = UserResponse(
            State.UNAUTHORIZED,
            message
        )

        fun success(id: String, username: String, email: String) = UserResponse(
            State.SUCCESS,
            "Task successful",
            id,
            username,
            email,
        )
    }
}