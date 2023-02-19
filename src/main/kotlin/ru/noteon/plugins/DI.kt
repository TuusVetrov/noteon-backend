package ru.noteon.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.util.*
import ru.noteon.di.component.AppComponent
import ru.noteon.di.component.ControllerComponent
import ru.noteon.di.component.DaggerAppComponent

fun Application.configureDI() {
    val appComponent = DaggerAppComponent.builder().withApplication(this).build()

    attributes.put(appComponentKey, appComponent)
    attributes.put(controllerComponentKey, appComponent.controllerComponent())
}

val appComponentKey = AttributeKey<AppComponent>("NOTEON_APP_COMPONENT")
val controllerComponentKey = AttributeKey<ControllerComponent>("NOTEON_CONTROLLER_COMPONENT")

val Application.appComponent: AppComponent get() = attributes[appComponentKey]

val Application.controllers: ControllerComponent get() = attributes[controllerComponentKey]

val Route.controllers: ControllerComponent get() = application.attributes[controllerComponentKey]