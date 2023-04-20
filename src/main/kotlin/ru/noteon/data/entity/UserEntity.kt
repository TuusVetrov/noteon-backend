package ru.noteon.data.entity

import ru.noteon.data.database.tables.UserTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserEntity>(UserTable)

    var username by UserTable.username
    var email by UserTable.email
    var password by UserTable.password
    var isActivated by UserTable.isActivated
}