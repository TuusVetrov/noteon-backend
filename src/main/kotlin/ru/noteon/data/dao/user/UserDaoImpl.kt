package ru.noteon.data.dao.user

import ru.noteon.data.database.DatabaseFactory
import ru.noteon.data.database.tables.UserTable
import ru.noteon.data.entity.UserEntity
import ru.noteon.data.model.UserModel
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDaoImpl @Inject constructor() : UserDaoFacade {
    override suspend fun addUser(email: String, password: String, username: String): UserModel = DatabaseFactory.dbQuery {
        UserEntity.new {
            this.email = email
            this.password = password
            this.username = username
        }
    }.let { UserModel.fromEntity(it) }

    override suspend fun findByUUID(uuid: UUID): UserModel? = DatabaseFactory.dbQuery {
        UserEntity.findById(uuid)
    }?.let { UserModel.fromEntity(it) }

    override suspend fun findByEmail(email: String): UserModel? = DatabaseFactory.dbQuery {
        UserEntity.find{
            UserTable.email eq email
        }.firstOrNull()
    }?.let { UserModel.fromEntity(it) }

    override suspend fun isUserExists(uuid: UUID): Boolean = findByUUID(uuid) != null

    override suspend fun isUsernameAvailable(username: String): Boolean = DatabaseFactory.dbQuery {
        UserEntity.find {
            UserTable.username eq username
        }.firstOrNull()
    } == null

    override suspend fun isEmailAvailable(email: String): Boolean = DatabaseFactory.dbQuery {
        UserEntity.find {
            UserTable.email eq email
        }.firstOrNull()
    } == null
}