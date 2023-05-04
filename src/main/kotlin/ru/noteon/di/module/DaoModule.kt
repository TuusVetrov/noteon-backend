package ru.noteon.di.module

import dagger.Binds
import dagger.Module
import ru.noteon.data.dao.folder.FolderDaoFacade
import ru.noteon.data.dao.folder.FolderDaoImpl
import ru.noteon.data.dao.note.NoteDaoFacade
import ru.noteon.data.dao.note.NoteDaoImpl
import ru.noteon.data.dao.token.TokenDaoFacade
import ru.noteon.data.dao.token.TokenDaoImpl
import ru.noteon.data.dao.user.UserDaoFacade
import ru.noteon.data.dao.user.UserDaoImpl
import javax.inject.Singleton

@Module
interface DaoModule {
    @Singleton
    @Binds
    fun userDao(dao: UserDaoImpl): UserDaoFacade

    @Singleton
    @Binds
    fun noteDao(dao: NoteDaoImpl): NoteDaoFacade

    @Singleton
    @Binds
    fun folderDao(dao: FolderDaoImpl): FolderDaoFacade

    @Singleton
    @Binds
    fun tokenDao(dao: TokenDaoImpl): TokenDaoFacade
}