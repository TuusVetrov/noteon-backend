package ru.noteon

import io.ktor.server.application.*
import ru.noteon.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    configureDI()
    configureCORS()
    configureAuthentication()
    configureStatusPages()
    configureContentNegotiation()
    configureRouting()
    configureDatabase()
}