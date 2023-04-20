package ru.noteon.data.database.tables

import org.jetbrains.exposed.dao.id.UUIDTable

object TokenTable: UUIDTable() {
    val user = reference("user", UserTable)
    val refreshToken = text("refreshToken")
}