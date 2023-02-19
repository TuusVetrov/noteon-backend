package ru.noteon.data.model

import ru.noteon.data.entity.UserEntity

data class User(
    val id: String,
    val username: String,
    val password: String,
    val email: String
) {
    companion object {
        fun fromEntity(entity: UserEntity) = User(
            entity.id.value.toString(),
            entity.username,
            entity.password,
            entity.email
        )
    }
}
