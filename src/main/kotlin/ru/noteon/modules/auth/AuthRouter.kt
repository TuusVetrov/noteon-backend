package ru.noteon.modules.auth

import dagger.Lazy
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import ru.noteon.modules.exception.BadRequestException
import ru.noteon.modules.exception.ExceptionMessages
import ru.noteon.data.model.response.generateHttpResponse
import ru.noteon.data.model.request.LoginRequest
import ru.noteon.data.model.request.RegistrationRequest
import ru.noteon.plugins.controllers

@Serializable
data class RefreshTokenRequest(
    val refreshToken: String
)
fun Route.authApi(authController: Lazy<AuthController> = controllers.authController()) {
    route("/auth") {
        controllers
        post("/register") {
            val authRequest = runCatching { call.receive<RegistrationRequest>() }.getOrElse {
                throw BadRequestException(ExceptionMessages.MESSAGE_MISSING_CREDENTIALS)
            }

            val authResponse = authController.get().register(authRequest.username, authRequest.email, authRequest.password)
            val response = generateHttpResponse(authResponse)

            call.respond(response.code, response.body)
        }

        post("/login") {
            val authRequest = runCatching { call.receive<LoginRequest>() }.getOrElse {
                throw BadRequestException(ExceptionMessages.MESSAGE_MISSING_CREDENTIALS)
            }

            val authResponse = authController.get().login(authRequest.email, authRequest.password)
            val response = generateHttpResponse(authResponse)

            call.respond(response.code, response.body)
        }

        get("/refresh-token") {
            val authorizationHeader = call.request.header("Authorization")
                ?: throw BadRequestException(ExceptionMessages.MESSAGE_MISSING_REFRESH_TOKEN)

            val refreshToken = authorizationHeader.removePrefix("Bearer ")

            val authResponse = authController.get().updateToken(refreshToken)
            val response = generateHttpResponse(authResponse)

            call.respond(response.code, response.body)
        }

    }
}