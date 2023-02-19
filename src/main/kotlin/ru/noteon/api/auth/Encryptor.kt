package ru.noteon.api.auth

import io.ktor.util.*
import ru.noteon.di.module.SecretKey
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

interface Encryptor {
    fun encrypt(data: String): String
}

class NoteonEncryptor @Inject constructor(@SecretKey secret: String) : Encryptor {
    private val hmacKey: SecretKeySpec = SecretKeySpec(secret.toByteArray(), ALGORITHM)

    override fun encrypt(data: String): String {
        val hmac = Mac.getInstance(ALGORITHM)
        hmac.init(hmacKey)
        return hex(hmac.doFinal(data.toByteArray(Charsets.UTF_8)))
    }

    companion object {
        private const val ALGORITHM = "HmacSHA256"
    }
}