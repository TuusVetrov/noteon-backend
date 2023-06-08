package ru.noteon.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import ru.noteon.modules.auth.authApi
import ru.noteon.modules.folders.folderApi
import ru.noteon.modules.notes.noteApi
import ru.noteon.modules.users.userApi

fun Application.configureRouting() {
    routing {
        route("/api") {
            authApi()
            noteApi()
            folderApi()
            userApi()
        }
    }
}

