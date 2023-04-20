package ru.noteon.data.dao.token

import ru.noteon.data.database.DatabaseFactory
import ru.noteon.data.database.tables.TokenTable
import ru.noteon.data.entity.TokenEntity
import ru.noteon.data.entity.UserEntity
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenDaoImpl @Inject constructor() : TokenDaoFacade {

    override suspend fun add(userId: String, refreshToken: String): String = DatabaseFactory.dbQuery {
        TokenEntity.new {
            this.user = UserEntity[UUID.fromString(userId)]
            this.refreshToken = refreshToken
        }
    }.id.value.toString()

    override suspend fun update(userId: String, refreshToken: String): String = DatabaseFactory.dbQuery {
        val user = UserEntity.findById(UUID.fromString(userId)) ?: throw NoSuchElementException("User not found")

        val token = TokenEntity.find { TokenTable.user eq user.id }.firstOrNull() ?: throw NoSuchElementException("Token not found")

        token.refreshToken = refreshToken
        token.flush()

        token.id.toString()
    }

    override suspend fun isTokenExists(userId: String): Boolean = DatabaseFactory.dbQuery {
        TokenEntity.find{
            TokenTable.user eq UUID.fromString(userId)
        }.firstOrNull() != null
    }

    override suspend fun saveToken(userId: String, refreshToken: String) = DatabaseFactory.dbQuery {
        if (isTokenExists(userId)) {
            update(userId, refreshToken)
            return@dbQuery
        }

        add(userId, refreshToken)
        return@dbQuery
    }

    override suspend fun removeToken(refreshToken: String): Boolean = DatabaseFactory.dbQuery {
        val tokenData = findToken(refreshToken)

        tokenData?.run {
            delete()
            return@dbQuery true
        }

        return@dbQuery false
    }

    override suspend fun findToken(refreshToken: String): TokenEntity? = DatabaseFactory.dbQuery {
        TokenEntity.find{
            TokenTable.refreshToken eq refreshToken
        }.firstOrNull()
    }
}