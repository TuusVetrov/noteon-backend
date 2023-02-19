package ru.noteon.plugins

import io.ktor.server.application.*
import ru.noteon.data.database.DatabaseProvider

fun Application.configureDatabase() {
    val databaseConfig = appComponent.configComponent().databaseConfig()
    DatabaseProvider.initDatabase(databaseConfig)
}