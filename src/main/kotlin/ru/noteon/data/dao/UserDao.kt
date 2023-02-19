package ru.noteon.data.dao

import ru.noteon.data.model.User
import java.util.*

interface UserDao {
    suspend fun addUser(email: String, username: String, password: String): User

    suspend fun findByUUID(uuid: UUID): User?
    suspend fun findByUsernameAndPassword(username: String, password: String): User?

    suspend fun isUserExists(uuid: UUID): Boolean
    suspend fun isUsernameAvailable(username: String): Boolean
    suspend fun isEmailAvailable(email: String): Boolean
}