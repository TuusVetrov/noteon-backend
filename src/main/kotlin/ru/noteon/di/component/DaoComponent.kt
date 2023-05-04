package ru.noteon.di.component

import dagger.Lazy
import dagger.Subcomponent
import ru.noteon.data.dao.folder.FolderDaoFacade
import ru.noteon.data.dao.note.NoteDaoFacade
import ru.noteon.data.dao.token.TokenDaoFacade
import ru.noteon.data.dao.user.UserDaoFacade
import ru.noteon.di.module.DaoModule
import javax.inject.Singleton

@Singleton
@Subcomponent(modules = [DaoModule::class])
interface DaoComponent {
    fun userDao(): Lazy<UserDaoFacade>
    fun noteDao(): Lazy<NoteDaoFacade>
    fun folderDao(): Lazy<FolderDaoFacade>
    fun tokenDao(): Lazy<TokenDaoFacade>
}