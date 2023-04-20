package ru.noteon.data.entity

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.noteon.data.database.tables.TokenTable
import java.util.*

class TokenEntity(id: EntityID<UUID>): UUIDEntity(id) {
    companion object : UUIDEntityClass<TokenEntity>(TokenTable)

    var user by UserEntity referencedOn TokenTable.user
    var refreshToken by TokenTable.refreshToken
}