package ru.noteon.data.model

import ru.noteon.data.entity.NoteEntity

data class Note(
    val id: String,
    val title: String,
    val body: String,
    val created: Long,
    val isPinned: Boolean
) {
    companion object {
        fun fromEntity(entity: NoteEntity) = Note(
            entity.id.value.toString(),
            entity.title,
            entity.body,
            entity.created.millis,
            entity.isPinned
        )
    }
}