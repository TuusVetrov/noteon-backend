package ru.noteon.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationRequest(
    val email: String,
    val password: String,
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
)