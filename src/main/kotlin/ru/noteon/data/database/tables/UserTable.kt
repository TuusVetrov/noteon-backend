package ru.noteon.data.database.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import ru.noteon.config.AppConstants.MAX_EMAIL_LENGTH
import ru.noteon.config.AppConstants.MAX_USERNAME_LENGTH

object UserTable: UUIDTable() {
    val username = varchar("username", length = MAX_USERNAME_LENGTH)
    val email = varchar("email", length = MAX_EMAIL_LENGTH).uniqueIndex() // standard RFC 5321
    val password = text("password")
    val isActivated = bool("isActivated").default(false)
}