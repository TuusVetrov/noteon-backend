package ru.noteon.di.module

import dagger.Binds
import dagger.Module
import ru.noteon.utils.Encryptor
import ru.noteon.utils.NoteonEncryptor
import javax.inject.Singleton

@Module
interface EncryptorModule {
    @Singleton
    @Binds
    abstract fun encryptor(encryptor: NoteonEncryptor): Encryptor
}