package ru.noteon.data.dao

import ru.noteon.data.model.Note

interface NoteDao {
    suspend fun add(userId: String, title: String, body: String): String
    suspend fun deleteById(id: String): Boolean
    suspend fun getAllByUser(userId: String): List<Note>
    suspend fun update(id: String, title: String, body: String): String
    suspend fun updateNotePinById(id: String, isPinned: Boolean): String

    suspend fun isNoteOwnedByUser(id: String, userId: String): Boolean
    suspend fun isNoteExists(id: String): Boolean
}