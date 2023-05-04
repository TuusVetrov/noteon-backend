package ru.noteon.data.dao.note

import org.jetbrains.exposed.sql.and
import ru.noteon.data.database.DatabaseFactory
import ru.noteon.data.database.tables.NoteTable
import ru.noteon.data.entity.FolderEntity
import ru.noteon.data.entity.NoteEntity
import ru.noteon.data.entity.UserEntity
import ru.noteon.data.model.NoteModel
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteDaoImpl @Inject constructor() : NoteDaoFacade {
    override suspend fun add(userId: String, folderId: String, title: String, body: String): String =
        DatabaseFactory.dbQuery {
            NoteEntity.new {
                this.author = UserEntity[UUID.fromString(userId)]
                this.folder = FolderEntity[UUID.fromString(folderId)]
                this.title = title
                this.body = body
            }
    }.id.value.toString()

    override suspend fun deleteById(id: String): Boolean = DatabaseFactory.dbQuery {
        val note = NoteEntity.findById(UUID.fromString(id))

        note?.run {
            delete()
            return@dbQuery true
        }

        return@dbQuery false
    }

    override suspend fun getAllByUser(userId: String): List<NoteModel> = DatabaseFactory.dbQuery {
        NoteEntity.find {NoteTable.author eq UUID.fromString(userId)}
            .sortedWith(compareBy({it.isPinned}, {it.updated}))
            .reversed()
            .map { NoteModel.fromEntity(it) }
    }

    override suspend fun getAllFromFolder(userId: String, folderId: String): List<NoteModel> = DatabaseFactory.dbQuery {
        NoteEntity.find {
            (NoteTable.author eq UUID.fromString(userId)) and (NoteTable.folder eq UUID.fromString(folderId))
        }
            .sortedWith(compareBy({it.isPinned}, {it.updated}))
            .reversed()
            .map { NoteModel.fromEntity(it) }
    }

    override suspend fun update(id: String, folderId: String, title: String, body: String): String =
        DatabaseFactory.dbQuery {
            NoteEntity[UUID.fromString(id)].apply {
                this.title = title
                this.body = body
                this.folder = FolderEntity[UUID.fromString(folderId)]
            }.id.value.toString()
    }

    override suspend fun updateNotePinById(id: String, isPinned: Boolean): String = DatabaseFactory.dbQuery {
        NoteEntity[UUID.fromString(id)].apply {
            this.isPinned = isPinned
        }.id.value.toString()
    }

    override suspend fun isNoteOwnedByUser(id: String, userId: String): Boolean = DatabaseFactory.dbQuery {
        NoteEntity.find {
            (NoteTable.id eq UUID.fromString(id)) and (NoteTable.author eq UUID.fromString(userId))
        }.firstOrNull() != null
    }

    override suspend fun isNoteExists(id: String): Boolean = DatabaseFactory.dbQuery {
        NoteEntity.findById(UUID.fromString(id)) != null
    }
}