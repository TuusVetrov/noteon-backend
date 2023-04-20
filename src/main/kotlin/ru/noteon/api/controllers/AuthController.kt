package ru.noteon.api.controllers

import com.auth0.jwt.JWT
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import ru.noteon.api.auth.Encryptor
import ru.noteon.api.auth.JWTController
import ru.noteon.api.auth.NoteonJWTController
import ru.noteon.api.exception.*
import ru.noteon.api.exception.ExceptionMessages
import ru.noteon.data.dao.token.TokenDaoFacade
import ru.noteon.data.model.response.AuthResponse
import ru.noteon.data.dao.user.UserDaoFacade
import ru.noteon.config.AppConstants.MAX_USERNAME_LENGTH
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthController @Inject constructor(
    private val userDao: UserDaoFacade,
    private val tokenDao: TokenDaoFacade,
    private val jwt: JWTController,
    private val encryptor: Encryptor
) {
    suspend fun register(email: String, password: String): AuthResponse {
        return try {
            validateCredentialsOrThrowException(email, password)

            if (!userDao.isEmailAvailable(email)) {
                throw BadRequestException("User with this email already exists")
            }

            val username = extractUsernameFromEmail(email)

            val user = userDao.addUser(email, encryptor.encrypt(password), username)
            val tokens = jwt.generateTokens(user.id)

            tokenDao.saveToken(user.id, tokens.second)

            AuthResponse.success(tokens.first, tokens.second, "Registration successful")
        } catch (bre: BadRequestException) {
            AuthResponse.failed(bre.message)
        }
    }

    suspend fun login(email: String, password: String): AuthResponse {
        return try {
            validateCredentialsOrThrowException(email, password)

            val user = userDao.findByEmail(email) ?: throw UnauthorizedActivityException("Invalid credentials")
            val passwordsEquals = encryptor.validate(password, user.password)

            if (!passwordsEquals) {
                throw UnauthorizedActivityException("Invalid credentials")
            }

            val tokens = jwt.generateTokens(user.id)

            tokenDao.saveToken(user.id, tokens.second)

            AuthResponse.success(tokens.first, tokens.second, "Login successful")
        } catch (bre: BadRequestException) {
            AuthResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            AuthResponse.unauthorized(uae.message)
        }
    }

    suspend fun updateToken(refreshToken: String): AuthResponse {
        return try {
            val userId = validateRefreshToken(refreshToken)

            val user = userDao.findByUUID(UUID.fromString(userId))
                ?: throw UnauthorizedActivityException(ExceptionMessages.MESSAGE_REFRESH_TOKEN_INVALID)
            val tokenFromBD = tokenDao.findToken(refreshToken)
                ?: throw UnauthorizedActivityException(ExceptionMessages.MESSAGE_REFRESH_TOKEN_INVALID)

            val newTokens = jwt.generateTokens(user.id)

            tokenDao.saveToken(user.id, newTokens.second)

            AuthResponse.success(newTokens.first, newTokens.second, "Tokens updated successful")
        } catch (uae: UnauthorizedActivityException) {
            AuthResponse.unauthorized(uae.message)
        }
    }

    // TODO:  Дописать проверку email
    private fun validateCredentialsOrThrowException(email: String, password: String) {
        val message = when {
            (email.isBlank() or password.isBlank()) -> "Email or password should not be blank"
            (email.length !in (6..254)) -> "Email should be of min 6 and max 254 character in length"
            else -> return
        }
    }

    /**
     * Validate refresh token from [refreshToken] and return userID
     */
    private fun validateRefreshToken(refreshToken: String): String {
        try {
            val jwtVerifier = jwt.verifyRefreshToken
            val decodedJWT = JWT.decode(refreshToken)
            jwtVerifier.verify(decodedJWT)
            return decodedJWT.getClaim(NoteonJWTController.ClAIM).asString()
        } catch (e: TokenExpiredException) {
            throw UnauthorizedActivityException(ExceptionMessages.MESSAGE_REFRESH_TOKEN_EXPIRED_EXCEPTION)
        } catch (e: JWTVerificationException) {
            throw UnauthorizedActivityException(ExceptionMessages.MESSAGE_REFRESH_TOKEN_INVALID)
        }
    }

    private fun extractUsernameFromEmail(email: String): String {
        var index = email.indexOf("@")

        if(index > MAX_USERNAME_LENGTH)
            index = MAX_USERNAME_LENGTH

        return email.substring(0, index)
    }

}