package ru.noteon.api.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import ru.noteon.di.module.SecretKey
import javax.inject.Inject

interface JWTController {
    val verifier: JWTVerifier

    fun sign(data: String): String
}

class NoteonJWTController @Inject constructor(@SecretKey secret: String) : JWTController {
    private val algorithm = Algorithm.HMAC256(secret)
    override val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .build()

    /**
     * Generates JWT token from [userId].
     */
    override fun sign(data: String): String = JWT
        .create()
        .withIssuer(ISSUER)
        .withAudience(AUDIENCE)
        .withClaim(ClAIM, data)
        .sign(algorithm)

    companion object {
        private const val ISSUER = "Noteon-JWT-Issuer"
        private const val AUDIENCE = "https://localhost:8080"
        const val ClAIM = "userId"
    }
}