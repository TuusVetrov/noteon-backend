package ru.noteon.data.dao

import org.jetbrains.exposed.sql.and
import ru.noteon.data.database.DatabaseProvider
import ru.noteon.data.database.tables.UserTable
import ru.noteon.data.entity.UserEntity
import ru.noteon.data.model.User
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDaoImpl @Inject constructor() : UserDao {
    override suspend fun addUser(email: String, username: String, password: String): User = DatabaseProvider.dbQuery {
        UserEntity.new {
            this.email = email
            this.username = username
            this.password = password
        }
    }.let { User.fromEntity(it) }

    override suspend fun findByUUID(uuid: UUID): User? = DatabaseProvider.dbQuery {
        UserEntity.findById(uuid)
    }?.let { User.fromEntity(it) }

    override suspend fun findByUsernameAndPassword(username: String, password: String): User? = DatabaseProvider.dbQuery {
        UserEntity.find{
            (UserTable.username eq username) and (UserTable.password eq password)
        }.firstOrNull()
    }?.let { User.fromEntity(it) }

    override suspend fun isUserExists(uuid: UUID): Boolean = findByUUID(uuid) != null

    override suspend fun isUsernameAvailable(username: String): Boolean = DatabaseProvider.dbQuery {
        UserEntity.find {
            UserTable.username eq username
        }.firstOrNull()
    } == null

    override suspend fun isEmailAvailable(email: String): Boolean = DatabaseProvider.dbQuery {
        UserEntity.find {
            UserTable.email eq email
        }.firstOrNull()
    } == null

}