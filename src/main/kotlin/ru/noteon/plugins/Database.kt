package ru.noteon.plugins

import io.ktor.server.application.*
import ru.noteon.data.database.DatabaseFactory

fun Application.configureDatabase() {
    val databaseConfig = appComponent.configComponent().databaseConfig()
    DatabaseFactory.initDatabase(databaseConfig)
}