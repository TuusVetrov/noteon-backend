package ru.noteon.modules.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import ru.noteon.di.module.AccessSecretKey
import ru.noteon.di.module.RefreshSecretKey
import java.util.*
import javax.inject.Inject

interface JWTController {
    val verifyAccessToken: JWTVerifier
    val verifyRefreshToken: JWTVerifier

    fun generateTokens(data: String): Pair<String, String>
}

class NoteonJWTController @Inject constructor(@AccessSecretKey accessSecretKey: String,
                                              @RefreshSecretKey refreshSecretKey: String) : JWTController {
    private val algorithmAccessToken = Algorithm.HMAC256(accessSecretKey)
    private val algorithmRefreshToken = Algorithm.HMAC256(refreshSecretKey)

    override val verifyAccessToken: JWTVerifier = JWT
        .require(algorithmAccessToken)
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .build()

    override val verifyRefreshToken: JWTVerifier = JWT
        .require(algorithmRefreshToken)
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .build()

    /**
     * GeneratesJWT access and refresh tokens from [userId].
     */
    override fun generateTokens(data: String): Pair<String, String> {
        val accessToken = JWT
            .create()
            .withIssuer(ISSUER)
            .withAudience(AUDIENCE)
            .withClaim(ClAIM, data)
            .withExpiresAt(Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
            .sign(algorithmAccessToken)

        val refreshToken = JWT
            .create()
            .withIssuer(ISSUER)
            .withAudience(AUDIENCE)
            .withClaim(ClAIM, data)
            .withExpiresAt(Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
            .sign(algorithmRefreshToken)

        return accessToken to refreshToken
    }

    companion object {
        private const val ISSUER = "Noteon-JWT-Issuer"
        private const val AUDIENCE = "https://localhost:8080"
        const val ClAIM = "userId"
        private const val ACCESS_TOKEN_VALIDITY = 30 * 24 * 60 * 60 * 1000L//30 * 60 * 1000L // 30 minutes
        private const val REFRESH_TOKEN_VALIDITY = 30 * 24 * 60 * 60 * 1000L // 30 days
    }
}