package ru.noteon.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class FolderRequest (
    val folderName: String
)