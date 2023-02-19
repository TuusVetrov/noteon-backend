package ru.noteon.data.database.tables

import org.jetbrains.exposed.dao.id.UUIDTable

object UserTable: UUIDTable() {
    val username = varchar("username", length = 256)
    val password = text("password")
    val email = varchar("email", length = 256)
}