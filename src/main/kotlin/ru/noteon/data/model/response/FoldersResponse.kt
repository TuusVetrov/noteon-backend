package ru.noteon.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class Folder(val id: String, val folderName: String)

@Serializable
data class FoldersListResponse (
    override val status: State,
    override val message: String,
    val folders: List<Folder> = emptyList(),
): Response {
    companion object {
        fun unauthorized(message: String) = FoldersListResponse(
            State.UNAUTHORIZED,
            message,
        )

        fun success(folders: List<Folder>) = FoldersListResponse(
            State.SUCCESS,
            "Task successful",
            folders
        )
    }
}

@Serializable
data class FolderResponse(
    override val status: State,
    override val message: String,
    val folderId: String? = null
): Response {
    companion object {
        fun unauthorized(message: String) = FolderResponse(
            State.UNAUTHORIZED,
            message
        )

        fun failed(message: String) = FolderResponse(
            State.FAILED,
            message
        )

        fun notFound(message: String) = FolderResponse(
            State.NOT_FOUND,
            message
        )

        fun success(id: String) = FolderResponse(
            State.SUCCESS,
            "Task successful",
            id
        )
    }
}