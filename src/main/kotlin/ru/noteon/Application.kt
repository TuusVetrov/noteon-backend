package ru.noteon

import io.ktor.server.application.*
import io.ktor.server.config.*
import ru.noteon.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    configureDI()
    configureCORS()
    configureAuthentication()
    configureStatusPages()
    configureContentNegotiation()
    configureRouting()
    configureDatabase()
}