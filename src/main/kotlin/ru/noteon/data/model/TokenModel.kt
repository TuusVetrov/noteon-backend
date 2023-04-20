package ru.noteon.data.model

import ru.noteon.data.entity.TokenEntity

data class TokenModel(
    val id: String,
    val refreshToken: String,
) {
    companion object {
        fun fromEntity(entity: TokenEntity) = TokenModel(
            entity.id.value.toString(),
            entity.refreshToken
        )
    }
}