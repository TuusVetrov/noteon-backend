package ru.noteon.di.module

import dagger.Binds
import dagger.Module
import ru.noteon.data.dao.NoteDao
import ru.noteon.data.dao.NoteDaoImpl
import ru.noteon.data.dao.UserDao
import ru.noteon.data.dao.UserDaoImpl
import javax.inject.Singleton

@Module
interface DaoModule {
    @Singleton
    @Binds
    fun userDao(dao: UserDaoImpl): UserDao

    @Singleton
    @Binds
    fun noteDao(dao: NoteDaoImpl): NoteDao
}