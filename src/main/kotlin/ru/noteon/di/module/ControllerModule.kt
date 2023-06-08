package ru.noteon.di.module

import dagger.Binds
import dagger.Module
import ru.noteon.modules.auth.JWTController
import ru.noteon.modules.auth.NoteonJWTController
import javax.inject.Singleton

@Module
interface ControllerModule {
    @Singleton
    @Binds
    fun jwtController(controller: NoteonJWTController): JWTController
}