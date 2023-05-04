package ru.noteon.data.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import ru.noteon.config.DatabaseConfig
import ru.noteon.data.database.tables.FolderTable
import ru.noteon.data.database.tables.NoteTable
import ru.noteon.data.database.tables.TokenTable
import ru.noteon.data.database.tables.UserTable

object DatabaseFactory {
    fun initDatabase(databaseConfig: DatabaseConfig) {
        val tables = arrayOf(UserTable, NoteTable, TokenTable, FolderTable)

        Database.connect(createDataSource(databaseConfig))

        transaction {
            SchemaUtils.createMissingTablesAndColumns(*tables)
        }
    }

    private fun createDataSource(databaseConfig: DatabaseConfig): HikariDataSource {
        val config = HikariConfig()
        with(databaseConfig) {
            config.driverClassName = driver
            config.password = password
            config.username = user
            config.jdbcUrl = "jdbc:postgresql://$host:$port/$name"
            config.maximumPoolSize = maxPoolSize
        }
        config.validate()
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}