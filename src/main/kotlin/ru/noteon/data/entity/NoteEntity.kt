package ru.noteon.data.entity

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.noteon.data.database.tables.NoteTable
import java.util.UUID

class NoteEntity(id: EntityID<UUID>): UUIDEntity(id) {
    companion object : UUIDEntityClass<NoteEntity>(NoteTable)

    var author by UserEntity referencedOn NoteTable.author
    var title by NoteTable.title
    var body by  NoteTable.body
    var created by NoteTable.created
    var updated by NoteTable.updated
    var isPinned by NoteTable.isPinned
}