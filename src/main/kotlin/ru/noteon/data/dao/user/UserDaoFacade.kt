package ru.noteon.data.dao.user

import ru.noteon.data.model.UserModel
import java.util.*

interface UserDaoFacade {
    suspend fun addUser(email: String, password: String, username: String): UserModel

    suspend fun updateUserData(userId: String, newUsername: String, newEmail: String): String

    suspend fun updateUserPassword(userId: String, newPassword: String): String

    suspend fun findByUUID(uuid: UUID): UserModel?
    suspend fun findByEmail(email: String): UserModel?

    suspend fun isUserExists(uuid: UUID): Boolean
    suspend fun isUsernameAvailable(username: String): Boolean
    suspend fun isEmailAvailable(email: String): Boolean
}