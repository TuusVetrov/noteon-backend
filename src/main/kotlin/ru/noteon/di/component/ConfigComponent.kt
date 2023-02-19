package ru.noteon.di.component

import dagger.Subcomponent
import ru.noteon.config.DatabaseConfig
import ru.noteon.di.module.ConfigModule
import ru.noteon.di.module.SecretKey
import javax.inject.Singleton

@Singleton
@Subcomponent(modules = [ConfigModule::class])
interface ConfigComponent {
    @SecretKey
    fun secretKey(): String

    fun databaseConfig(): DatabaseConfig
}