package ru.noteon.api.controllers

import ru.noteon.api.auth.Encryptor
import ru.noteon.api.auth.JWTController
import ru.noteon.api.exception.*
import ru.noteon.api.models.response.AuthResponse
import ru.noteon.data.dao.UserDao
import ru.noteon.utils.isAlphaNumeric
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthController @Inject constructor(
    private val userDao: UserDao,
    private val jwt: JWTController,
    private val encryptor: Encryptor
) {
    suspend fun register(email: String, username: String, password: String): AuthResponse {
        return try {
            validateCredentialsOrThrowException(username, password)

            if (!userDao.isUsernameAvailable(username)) {
                throw BadRequestException("Username is not available")
            }

            val user = userDao.addUser(email, username, encryptor.encrypt(password))
            AuthResponse.success(jwt.sign(user.id), "Registration successful")
        } catch (bre: BadRequestException) {
            AuthResponse.failed(bre.message)
        }
    }

    suspend fun login(username: String, password: String): AuthResponse {
        return try {
            validateCredentialsOrThrowException(username, password)

            val user = userDao.findByUsernameAndPassword(username, encryptor.encrypt(password))
                ?: throw UnauthorizedActivityException("Invalid credentials")

            AuthResponse.success(jwt.sign(user.id), "Login successful")
        } catch (bre: BadRequestException) {
            AuthResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            AuthResponse.unauthorized(uae.message)
        }
    }

    private fun validateCredentialsOrThrowException(username: String, password: String) {
        val message = when {
            (username.isBlank() or password.isBlank()) -> "Username or password should not be blank"
            (username.length !in (4..30)) -> "Username should be of min 4 and max 30 character in length"
            (password.length !in (8..50)) -> "Password should be of min 8 and max 50 character in length"
            (!username.isAlphaNumeric()) -> "No special characters allowed in username"
            else -> return
        }

        throw BadRequestException(message)
    }
}