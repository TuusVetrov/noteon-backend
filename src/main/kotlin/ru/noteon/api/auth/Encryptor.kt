package ru.noteon.api.auth

import org.mindrot.jbcrypt.BCrypt
import javax.inject.Inject

interface Encryptor {
    fun encrypt(data: String): String
    fun validate(data: String, hashedData: String): Boolean
}

class NoteonEncryptor @Inject constructor() : Encryptor {
    override fun encrypt(data: String): String {
        val salt = BCrypt.gensalt()
        return BCrypt.hashpw(data, salt)
    }

    override fun validate(data: String, hashedData: String): Boolean {
        return BCrypt.checkpw(data, hashedData)
    }
}