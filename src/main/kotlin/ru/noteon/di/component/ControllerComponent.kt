package ru.noteon.di.component

import dagger.Lazy
import dagger.Subcomponent
import ru.noteon.modules.auth.JWTController
import ru.noteon.modules.auth.AuthController
import ru.noteon.modules.folders.FoldersController
import ru.noteon.modules.notes.NotesController
import ru.noteon.di.module.ConfigModule
import ru.noteon.di.module.ControllerModule
import ru.noteon.di.module.DaoModule
import ru.noteon.di.module.EncryptorModule
import ru.noteon.modules.users.UserProfileController
import javax.inject.Singleton

@Singleton
@Subcomponent(modules = [ConfigModule::class, EncryptorModule::class, ControllerModule::class, DaoModule::class])
interface ControllerComponent {
    fun authController(): Lazy<AuthController>
    fun notesController(): Lazy<NotesController>
    fun foldersController(): Lazy<FoldersController>
    fun userProfileController(): Lazy<UserProfileController>
    fun jwtController(): Lazy<JWTController>
}