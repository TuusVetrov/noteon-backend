package ru.noteon.data.dao

import org.jetbrains.exposed.sql.and
import ru.noteon.data.database.DatabaseProvider
import ru.noteon.data.database.tables.NoteTable
import ru.noteon.data.entity.NoteEntity
import ru.noteon.data.entity.UserEntity
import ru.noteon.data.model.Note
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteDaoImpl @Inject constructor() : NoteDao {
    override suspend fun add(userId: String, title: String, body: String): String = DatabaseProvider.dbQuery {
        NoteEntity.new {
            this.author = UserEntity[UUID.fromString(userId)]
            this.title = title
            this.body = body
        }
    }.id.value.toString()

    override suspend fun deleteById(id: String): Boolean = DatabaseProvider.dbQuery {
        val note = NoteEntity.findById(UUID.fromString(id))

        note?.run {
            delete()
            return@dbQuery true
        }

        return@dbQuery false
    }

    override suspend fun getAllByUser(userId: String): List<Note> = DatabaseProvider.dbQuery {
        NoteEntity.find {NoteTable.author eq UUID.fromString(userId)}
            .sortedWith(compareBy({it.isPinned}, {it.updated}))
            .reversed()
            .map { Note.fromEntity(it) }
    }

    override suspend fun update(id: String, title: String, body: String): String  = DatabaseProvider.dbQuery {
        NoteEntity[UUID.fromString(id)].apply {
            this.title = title
            this.body = body
        }.id.value.toString()
    }

    override suspend fun updateNotePinById(id: String, isPinned: Boolean): String = DatabaseProvider.dbQuery {
        NoteEntity[UUID.fromString(id)].apply {
            this.isPinned = isPinned
        }.id.value.toString()
    }

    override suspend fun isNoteOwnedByUser(id: String, userId: String): Boolean = DatabaseProvider.dbQuery {
        NoteEntity.find {
            (NoteTable.id eq UUID.fromString(id)) and (NoteTable.author eq UUID.fromString(userId))
        }.firstOrNull() != null
    }

    override suspend fun isNoteExists(id: String): Boolean = DatabaseProvider.dbQuery {
        NoteEntity.findById(UUID.fromString(id)) != null
    }

}