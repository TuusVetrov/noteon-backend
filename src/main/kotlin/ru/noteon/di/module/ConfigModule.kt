package ru.noteon.di.module

import dagger.Module
import dagger.Provides
import io.ktor.server.application.*
import io.ktor.server.config.*
import ru.noteon.config.DatabaseConfig
import javax.inject.Singleton

@Module
object ConfigModule {
    @Provides
    fun applicationConfig(application: Application) = application.environment.config

    @Singleton
    @Provides
    fun databaseConfig(config: ApplicationConfig): DatabaseConfig {
        val dbConfig = config.config("database")
        return DatabaseConfig(
            host = dbConfig.property("host").getString(),
            port = dbConfig.property("port").getString(),
            name = dbConfig.property("name").getString(),
            user = dbConfig.property("user").getString(),
            password = dbConfig.property("password").getString(),
            driver = dbConfig.property("driver").getString(),
            maxPoolSize = dbConfig.property("maxPoolSize").getString().toInt()
        )
    }

    @Singleton
    @Provides
    @AccessSecretKey
    fun accessSecretKey(config: ApplicationConfig): String = config.property("key.jwt_access_key").getString()

    @Singleton
    @Provides
    @RefreshSecretKey
    fun refreshSecretKey(config: ApplicationConfig): String = config.property("key.jwt_refresh_key").getString()
}