package ru.noteon.data.dao.folder

import ru.noteon.data.model.FolderModel
import ru.noteon.data.model.NoteModel

interface FolderDaoFacade {
    suspend fun add(userId: String, folderName: String): String
    suspend fun deleteById(id: String): Boolean
    suspend fun getAllByUser(userId: String): List<FolderModel>
    suspend fun update(id: String, newFolderName: String): String

    suspend fun isFolderOwnedByUser(id: String, userId: String): Boolean
    suspend fun isFolderExists(id: String): Boolean
}