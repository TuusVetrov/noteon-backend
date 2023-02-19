package ru.noteon.api.routes

import dagger.Lazy
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.noteon.api.auth.principal.UserPrincipal
import ru.noteon.api.controllers.NotesController
import ru.noteon.api.exception.BadRequestException
import ru.noteon.api.exception.ExceptionMessages
import ru.noteon.api.exception.UnauthorizedActivityException
import ru.noteon.api.models.request.NoteRequest
import ru.noteon.api.models.request.PinRequest
import ru.noteon.api.models.response.generateHttpResponse
import ru.noteon.plugins.controllers

fun Route.noteApi(notesController: Lazy<NotesController> = controllers.notesController()) {
    authenticate {
        get("/notes") {
            val principal = call.principal<UserPrincipal>()
                ?: throw UnauthorizedActivityException(ExceptionMessages.MESSAGE_ACCESS_DENIED)

            val notesResponse = notesController.get().getNotesByUser(principal.user)
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

                val noteResponse = notesController.get().addNote(principal.user, noteRequest)
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

                val noteResponse = notesController.get().updateNote(principal.user, noteId, noteRequest)
                val response = generateHttpResponse(noteResponse)

                call.respond(response.code, response.body)
            }

            delete("/{id}") {
                val noteId = call.parameters["id"] ?: return@delete
                val principal = call.principal<UserPrincipal>()
                    ?: throw UnauthorizedActivityException(ExceptionMessages.MESSAGE_ACCESS_DENIED)

                val noteResponse = notesController.get().deleteNote(principal.user, noteId)
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

                val noteResponse = notesController.get().updateNotePin(principal.user, noteId, pinRequest)
                val response = generateHttpResponse(noteResponse)

                call.respond(response.code, response.body)
            }
        }
    }
}