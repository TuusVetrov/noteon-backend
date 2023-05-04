package ru.noteon.data.dao.note

import ru.noteon.data.model.NoteModel

interface NoteDaoFacade {
    suspend fun add(userId: String, folderId: String, title: String, body: String): String
    suspend fun deleteById(id: String): Boolean
    suspend fun getAllByUser(userId: String): List<NoteModel>
    suspend fun getAllFromFolder(userId: String, folderId: String): List<NoteModel>
    suspend fun update(id: String, folderId: String, title: String, body: String): String
    suspend fun updateNotePinById(id: String, isPinned: Boolean): String

    suspend fun isNoteOwnedByUser(id: String, userId: String): Boolean
    suspend fun isNoteExists(id: String): Boolean
}