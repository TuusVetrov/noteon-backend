package ru.noteon.data.dao.folder

import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import ru.noteon.data.database.DatabaseFactory
import ru.noteon.data.database.tables.FolderTable
import ru.noteon.data.entity.FolderEntity
import ru.noteon.data.entity.UserEntity
import ru.noteon.data.model.FolderModel
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderDaoImpl @Inject constructor() : FolderDaoFacade {
    override suspend fun add(userId: String, folderName: String): String = DatabaseFactory.dbQuery {
        FolderEntity.new {
            this.folderName = folderName
            this.user = UserEntity[UUID.fromString(userId)]
        }
    }.id.value.toString()


    override suspend fun deleteById(id: String): Boolean = DatabaseFactory.dbQuery {
        val folder = FolderEntity.findById(UUID.fromString(id))

        folder?.run {
            delete()
            return@dbQuery true
        }

        return@dbQuery false
    }

    override suspend fun getAllByUser(userId: String): List<FolderModel> = DatabaseFactory.dbQuery {
        FolderEntity.find { FolderTable.user eq UUID.fromString(userId)}
            .orderBy(FolderTable.folderName to SortOrder.ASC)
            .map { FolderModel.fromEntity(it) }
    }

    override suspend fun update(id: String, newFolderName: String): String = DatabaseFactory.dbQuery {
        FolderEntity[UUID.fromString(id)].apply {
           this.folderName = newFolderName
        }
    }.id.value.toString()

    override suspend fun isFolderOwnedByUser(id: String, userId: String): Boolean = DatabaseFactory.dbQuery {
        FolderEntity.find {
            (FolderTable.id eq UUID.fromString(id)) and (FolderTable.user eq UUID.fromString(userId))
        }.firstOrNull() != null
    }

    override suspend fun isFolderExists(id: String): Boolean = DatabaseFactory.dbQuery {
        FolderEntity.findById(UUID.fromString(id))!= null
    }
}