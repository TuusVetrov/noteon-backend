package ru.noteon.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import ru.noteon.api.routes.authApi
import ru.noteon.api.routes.noteApi

fun Application.configureRouting() {
    routing {
        route("/api") {
            authApi()
            noteApi()
        }
    }
}

