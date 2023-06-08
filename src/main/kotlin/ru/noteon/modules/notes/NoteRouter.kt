package ru.noteon.modules.notes

import dagger.Lazy
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.noteon.data.model.principal.UserPrincipal
import ru.noteon.modules.exception.BadRequestException
import ru.noteon.modules.exception.ExceptionMessages
import ru.noteon.modules.exception.UnauthorizedActivityException
import ru.noteon.data.model.request.NoteRequest
import ru.noteon.data.model.request.PinRequest
import ru.noteon.data.model.response.generateHttpResponse
import ru.noteon.plugins.controllers

fun Route.noteApi(
    notesController: Lazy<NotesController> = controllers.notesController()
) {
    authenticate {
        get("/notes") {
            val principal = call.principal<UserPrincipal>()
                ?: throw UnauthorizedActivityException(ExceptionMessages.MESSAGE_ACCESS_DENIED)

            val notesResponse = notesController.get().getNotesByUser(principal.userModel)
            val response = generateHttpResponse(notesResponse)

            call.respond(response.code, response.body)
        }

        get("/{folderId}/notes") {
            val folderId = call.parameters["folderId"] ?: return@get
            val principal = call.principal<UserPrincipal>()
                ?: throw UnauthorizedActivityException(ExceptionMessages.MESSAGE_ACCESS_DENIED)

            val notesResponse = notesController.get().getAllFromFolder(principal.userModel, folderId)
            val response = generateHttpResponse(notesResponse)
            call.respond(response.code, response.body)
        }

        route("/note/") {
            post("/new") {
                val noteRequest = runCatching { call.receive<NoteRequest>() }.getOrElse {
                    throw BadRequestException(ExceptionMessages.MESSAGE_MISSING_NOTE_DETAILS)
                }

                val principal = call.principal<UserPrincipal>()
                    ?: throw UnauthorizedActivityException(ExceptionMessages.MESSAGE_ACCESS_DENIED)

                val noteResponse = notesController.get().addNote(principal.userModel, noteRequest)
                val response = generateHttpResponse(noteResponse)

                call.respond(response.code, response.body)
            }

            put("/{id}") {
                val noteId = call.parameters["id"] ?: return@put
                val noteRequest = runCatching { call.receive<NoteRequest>() }.getOrElse {
                    throw BadRequestException(ExceptionMessages.MESSAGE_MISSING_NOTE_DETAILS)
                }

                val principal = call.principal<UserPrincipal>()
                    ?: throw UnauthorizedActivityException(ExceptionMessages.MESSAGE_ACCESS_DENIED)

                val noteResponse = notesController.get().updateNote(principal.userModel, noteId, noteRequest)
                val response = generateHttpResponse(noteResponse)

                call.respond(response.code, response.body)
            }

            delete("/{id}") {
                val noteId = call.parameters["id"] ?: return@delete
                val principal = call.principal<UserPrincipal>()
                    ?: throw UnauthorizedActivityException(ExceptionMessages.MESSAGE_ACCESS_DENIED)

                val noteResponse = notesController.get().deleteNote(principal.userModel, noteId)
                val response = generateHttpResponse(noteResponse)

                call.respond(response.code, response.body)
            }

            patch("/{id}/pin") {
                val noteId = call.parameters["id"] ?: return@patch
                val pinRequest = runCatching { call.receive<PinRequest>() }.getOrElse {
                    throw BadRequestException(ExceptionMessages.MESSAGE_MISSING_PIN_DETAILS)
                }

                val principal = call.principal<UserPrincipal>()
                    ?: throw UnauthorizedActivityException(ExceptionMessages.MESSAGE_ACCESS_DENIED)

                val noteResponse = notesController.get().updateNotePin(principal.userModel, noteId, pinRequest)
                val response = generateHttpResponse(noteResponse)

                call.respond(response.code, response.body)
            }
        }
    }
}