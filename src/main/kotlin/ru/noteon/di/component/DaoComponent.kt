package ru.noteon.di.component

import dagger.Lazy
import dagger.Subcomponent
import ru.noteon.data.dao.NoteDao
import ru.noteon.data.dao.UserDao
import ru.noteon.di.module.DaoModule
import javax.inject.Singleton

@Singleton
@Subcomponent(modules = [DaoModule::class])
interface DaoComponent {
    fun userDao(): Lazy<UserDao>
    fun noteDao(): Lazy<NoteDao>
}