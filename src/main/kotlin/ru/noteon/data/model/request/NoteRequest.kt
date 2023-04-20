package ru.noteon.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class NoteRequest(
    val title: String,
    val body: String
)

@Serializable
data class PinRequest(
    val isPinned: Boolean
)