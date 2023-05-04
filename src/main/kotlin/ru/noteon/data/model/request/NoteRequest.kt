package ru.noteon.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class NoteRequest(
    val title: String,
    val body: String,
    val folderId: String,
)

@Serializable
data class PinRequest(
    val isPinned: Boolean
)