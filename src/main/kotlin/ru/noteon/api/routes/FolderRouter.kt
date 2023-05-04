package ru.noteon.api.routes

import dagger.Lazy
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.noteon.api.controllers.FoldersController
import ru.noteon.api.exception.BadRequestException
import ru.noteon.api.exception.ExceptionMessages
import ru.noteon.api.exception.UnauthorizedActivityException
import ru.noteon.data.model.principal.UserPrincipal
import ru.noteon.data.model.request.FolderRequest
import ru.noteon.data.model.response.generateHttpResponse
import ru.noteon.plugins.controllers

fun Route.folderApi(
    foldersController: Lazy<FoldersController> = controllers.foldersController()
) {
    authenticate {
        get("/folders") {
            val principal = call.principal<UserPrincipal>()
                ?: throw UnauthorizedActivityException(ExceptionMessages.MESSAGE_ACCESS_DENIED)

            val foldersResponse = foldersController.get().getFoldersByUser(principal.userModel)
            val response = generateHttpResponse(foldersResponse)

            call.respond(response.code, response.body)
        }

        route("/folder/") {
            post("/new") {
                val folderRequest = runCatching { call.receive<FolderRequest>() }.getOrElse {
                    throw BadRequestException(ExceptionMessages.MESSAGE_MISSING_FOLDER_DETAILS)
                }

                val principal = call.principal<UserPrincipal>()
                    ?: throw UnauthorizedActivityException(ExceptionMessages.MESSAGE_ACCESS_DENIED)

                val folderResponse = foldersController.get().addFolder(principal.userModel, folderRequest)
                val response = generateHttpResponse(folderResponse)

                call.respond(response.code, response.body)
            }

            put("/{id}") {
                val folderId = call.parameters["id"] ?: return@put
                val folderRequest = runCatching { call.receive<FolderRequest>() }.getOrElse {
                    throw BadRequestException(ExceptionMessages.MESSAGE_MISSING_FOLDER_DETAILS)
                }

                val principal = call.principal<UserPrincipal>()
                    ?: throw UnauthorizedActivityException(ExceptionMessages.MESSAGE_ACCESS_DENIED)

                val folderResponse = foldersController.get().updateFolder(principal.userModel, folderId, folderRequest)
                val response = generateHttpResponse(folderResponse)

                call.respond(response.code, response.body)
            }

            delete("/{id}") {
                val folderId = call.parameters["id"] ?: return@delete
                val principal = call.principal<UserPrincipal>()
                    ?: throw UnauthorizedActivityException(ExceptionMessages.MESSAGE_ACCESS_DENIED)

                val folderResponse = foldersController.get().deleteFolder(principal.userModel, folderId)
                val response = generateHttpResponse(folderResponse)

                call.respond(response.code, response.body)
            }
        }
    }
}