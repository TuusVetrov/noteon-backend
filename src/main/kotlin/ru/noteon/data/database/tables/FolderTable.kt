package ru.noteon.data.database.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import ru.noteon.config.AppConstants.MAX_FOLDER_NAME_LENGTH

object FolderTable: UUIDTable() {
    val folderName = varchar("folderName", length = MAX_FOLDER_NAME_LENGTH)
    val user = reference("user", UserTable)
}