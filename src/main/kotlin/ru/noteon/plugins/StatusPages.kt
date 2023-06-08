package ru.noteon.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import ru.noteon.modules.exception.BadRequestException
import ru.noteon.modules.exception.ExceptionMessages
import ru.noteon.data.model.response.FailureResponse
import ru.noteon.data.model.response.State

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<BadRequestException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, FailureResponse(State.FAILED, cause.message ?: "Bad Request"))
        }

        status(HttpStatusCode.InternalServerError) { call, cause ->
            call.respond(cause, FailureResponse(State.FAILED, ExceptionMessages.MESSAGE_FAILED))
        }

        status(HttpStatusCode.Unauthorized) { call, cause ->
            call.respond(cause, FailureResponse(State.UNAUTHORIZED, ExceptionMessages.MESSAGE_ACCESS_DENIED))
        }
    }
}