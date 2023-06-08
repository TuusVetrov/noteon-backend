package ru.noteon.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserDataRequest(
    val username: String,
    val email: String,
)

@Serializable
data class UpdateUserPassword(
    val password: String,
)