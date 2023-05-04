package ru.noteon.data.entity

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.noteon.data.database.tables.FolderTable
import java.util.*

class FolderEntity(id: EntityID<UUID>): UUIDEntity(id) {
    companion object : UUIDEntityClass<FolderEntity>(FolderTable)

    var folderName by FolderTable.folderName
    var user by UserEntity referencedOn FolderTable.user
}