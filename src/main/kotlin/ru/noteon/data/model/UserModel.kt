package ru.noteon.data.model

import ru.noteon.data.entity.UserEntity

data class UserModel(
    val id: String,
    val username: String,
    val email: String,
    val password: String,
    val isActivated: Boolean
) {
    companion object {
        fun fromEntity(entity: UserEntity) = UserModel(
            entity.id.value.toString(),
            entity.username,
            entity.email,
            entity.password,
            entity.isActivated,
        )
    }
}
