package ru.noteon.api.routes

import dagger.Lazy
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.noteon.api.controllers.AuthController
import ru.noteon.api.exception.BadRequestException
import ru.noteon.api.exception.ExceptionMessages
import ru.noteon.api.models.request.*
import ru.noteon.api.models.response.generateHttpResponse
import ru.noteon.plugins.controllers


fun Route.authApi(authController: Lazy<AuthController> = controllers.authController()) {
    route("/auth") {
        controllers
        post("/register") {
            val authRequest = runCatching { call.receive<RegistrationRequest>() }.getOrElse {
                throw BadRequestException(ExceptionMessages.MESSAGE_MISSING_CREDENTIALS)
            }

            val authResponse = authController.get().register(authRequest.email, authRequest.username, authRequest.password)
            val response = generateHttpResponse(authResponse)

            call.respond(response.code, response.body)
        }

        post("/login") {
            val authRequest = runCatching { call.receive<LoginRequest>() }.getOrElse {
                throw BadRequestException(ExceptionMessages.MESSAGE_MISSING_CREDENTIALS)
            }

            val authResponse = authController.get().login(authRequest.username, authRequest.password)
            val response = generateHttpResponse(authResponse)

            call.respond(response.code, response.body)
        }
    }
}