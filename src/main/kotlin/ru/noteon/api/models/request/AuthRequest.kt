package ru.noteon.api.models.request

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationRequest(
    val username: String,
    val password: String,
    val email: String
)

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)