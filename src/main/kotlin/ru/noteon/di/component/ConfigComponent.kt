package ru.noteon.di.component

import dagger.Subcomponent
import ru.noteon.config.DatabaseConfig
import ru.noteon.di.module.AccessSecretKey
import ru.noteon.di.module.ConfigModule
import ru.noteon.di.module.RefreshSecretKey
import javax.inject.Singleton

@Singleton
@Subcomponent(modules = [ConfigModule::class])
interface ConfigComponent {
    @AccessSecretKey
    fun accessSecretKey(): String
    @RefreshSecretKey
    fun refreshSecretKey(): String
    fun databaseConfig(): DatabaseConfig
}