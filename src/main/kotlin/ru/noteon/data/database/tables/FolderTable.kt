package ru.noteon.data.database.tables

import org.jetbrains.exposed.dao.id.UUIDTable

const val MAX_FOLDER_NAME_LENGTH = 50

object FolderTable: UUIDTable() {
    val folderName = varchar("folderName", length = MAX_FOLDER_NAME_LENGTH)
    val user = reference("user", UserTable)
}