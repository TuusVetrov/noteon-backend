package ru.noteon.api.controllers

import ru.noteon.api.exception.BadRequestException
import ru.noteon.data.dao.token.TokenDaoFacade
import ru.noteon.data.dao.user.UserDaoFacade
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileController @Inject constructor(
    private val userDao: UserDaoFacade,
    private val tokenDao: TokenDaoFacade,
) {
    suspend fun changeEmail(userId: UUID, newEmail: String) {

    }

    suspend fun changeUsername() {

    }

    suspend fun changePassword() {

    }

    suspend fun logout(refreshToken: String): Boolean {
        return tokenDao.removeToken(refreshToken)
    }
}