package ru.noteon.api.models.response

import kotlinx.serialization.Serializable

@Serializable
data class Note(val id: String, val title: String, val body: String, val created: Long, val isPinned: Boolean)

@Serializable
data class NotesListResponse(
    override val status: State,
    override val message: String,
    val notes: List<Note> = emptyList()
): Response {
    companion object {
        fun unauthorized(message: String) = NotesListResponse(
            State.UNAUTHORIZED,
            message
        )

        fun success(notes: List<Note>) = NotesListResponse(
            State.SUCCESS,
            "Task successful",
            notes
        )
    }
}

@Serializable
data class NoteResponse(
    override val status: State,
    override val message: String,
    val noteId: String? = null
): Response {
    companion object {
        fun unauthorized(message: String) = NoteResponse(
            State.UNAUTHORIZED,
            message
        )

        fun failed(message: String) = NoteResponse(
            State.FAILED,
            message
        )

        fun notFound(message: String) = NoteResponse(
            State.NOT_FOUND,
            message
        )

        fun success(id: String) = NoteResponse(
            State.SUCCESS,
            "Task successful",
            id
        )
    }
}