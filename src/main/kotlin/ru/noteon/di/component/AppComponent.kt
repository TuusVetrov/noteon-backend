package ru.noteon.di.component

import dagger.BindsInstance
import dagger.Component
import io.ktor.server.application.*

@Component
interface AppComponent {
    fun application(): Application
    fun controllerComponent(): ControllerComponent
    fun configComponent(): ConfigComponent
    fun daoComponent(): DaoComponent

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun withApplication(application: Application): Builder
        fun build(): AppComponent
    }
}