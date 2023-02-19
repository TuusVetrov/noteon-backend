package ru.noteon.di.component

import dagger.Lazy
import dagger.Subcomponent
import ru.noteon.api.auth.JWTController
import ru.noteon.api.controllers.AuthController
import ru.noteon.api.controllers.NotesController
import ru.noteon.di.module.ConfigModule
import ru.noteon.di.module.ControllerModule
import ru.noteon.di.module.DaoModule
import ru.noteon.di.module.EncryptorModule
import javax.inject.Singleton

@Singleton
@Subcomponent(modules = [ConfigModule::class, EncryptorModule::class, ControllerModule::class, DaoModule::class])
interface ControllerComponent {
    fun authController(): Lazy<AuthController>
    fun notesController(): Lazy<NotesController>
    fun jwtController(): Lazy<JWTController>
}