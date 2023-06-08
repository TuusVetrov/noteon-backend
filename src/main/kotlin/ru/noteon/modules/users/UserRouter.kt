package ru.noteon.modules.users

import dagger.Lazy
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.noteon.data.model.principal.UserPrincipal
import ru.noteon.data.model.request.FolderRequest
import ru.noteon.data.model.request.UpdateUserDataRequest
import ru.noteon.data.model.request.UpdateUserPassword
import ru.noteon.data.model.response.generateHttpResponse
import ru.noteon.modules.exception.BadRequestException
import ru.noteon.modules.exception.ExceptionMessages
import ru.noteon.modules.exception.UnauthorizedActivityException
import ru.noteon.modules.folders.FoldersController
import ru.noteon.plugins.controllers

fun Route.userApi(
    userProfileController: Lazy<UserProfileController> = controllers.userProfileController()
) {
    authenticate {
        get("/user") {
            val principal = call.principal<UserPrincipal>()
                ?: throw UnauthorizedActivityException(ExceptionMessages.MESSAGE_ACCESS_DENIED)

            val userResponse = userProfileController.get().getUserById(principal.userModel)
            val response = generateHttpResponse(userResponse)

            call.respond(response.code, response.body)
        }

        put("/user/change_data") {
            val userRequest = runCatching { call.receive<UpdateUserDataRequest>() }.getOrElse {
                throw BadRequestException(ExceptionMessages.MESSAGE_MISSING_FOLDER_DETAILS)
            }

            val principal = call.principal<UserPrincipal>()
                ?: throw UnauthorizedActivityException(ExceptionMessages.MESSAGE_ACCESS_DENIED)

            val userResponse = userProfileController.get().changeUserData(principal.userModel, userRequest.username, userRequest.email)
            val response = generateHttpResponse(userResponse)

            call.respond(response.code, response.body)
        }

        put("/user/change_password") {
            val userRequest = runCatching { call.receive<UpdateUserPassword>() }.getOrElse {
                throw BadRequestException(ExceptionMessages.MESSAGE_MISSING_FOLDER_DETAILS)
            }

            val principal = call.principal<UserPrincipal>()
                ?: throw UnauthorizedActivityException(ExceptionMessages.MESSAGE_ACCESS_DENIED)

            val userResponse = userProfileController.get().changeUserPassword(principal.userModel, userRequest.password)
            val response = generateHttpResponse(userResponse)

            call.respond(response.code, response.body)
        }
    }
}