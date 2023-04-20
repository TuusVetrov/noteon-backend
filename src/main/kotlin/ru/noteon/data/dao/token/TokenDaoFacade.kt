package ru.noteon.data.dao.token

import ru.noteon.data.entity.TokenEntity

interface TokenDaoFacade {
    suspend fun add(userId: String, refreshToken: String): String
    suspend fun update(userId: String, refreshToken: String): String
    suspend fun isTokenExists(userId: String): Boolean
    suspend fun saveToken(userId: String, refreshToken: String)
    suspend fun removeToken(refreshToken: String): Boolean
    suspend fun findToken(refreshToken: String): TokenEntity?
}