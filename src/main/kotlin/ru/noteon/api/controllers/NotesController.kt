package ru.noteon.api.controllers

import ru.noteon.api.exception.BadRequestException
import ru.noteon.api.exception.NoteNotFoundException
import ru.noteon.api.exception.UnauthorizedActivityException
import ru.noteon.api.models.request.NoteRequest
import ru.noteon.api.models.request.PinRequest
import ru.noteon.api.models.response.NotesListResponse
import ru.noteon.data.dao.NoteDao
import ru.noteon.data.model.User
import ru.noteon.api.models.response.Note
import ru.noteon.api.models.response.NoteResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesController @Inject constructor(private val noteDao: NoteDao) {
    suspend fun getNotesByUser(user: User): NotesListResponse {
        return try {
            val notes = noteDao.getAllByUser(user.id)

            NotesListResponse.success(notes.map { Note(it.id, it.title, it.body, it.created, it.isPinned) })
        } catch (uae: UnauthorizedActivityException) {
            NotesListResponse.unauthorized(uae.message)
        }
    }

    suspend fun addNote(user: User, note: NoteRequest): NoteResponse {
        return try {
            val noteTitle = note.title.trim()
            val noteText = note.body.trim()

            validateNoteOrThrowException(noteTitle, noteText)

            val noteId = noteDao.add(user.id, noteTitle, noteText)
            NoteResponse.success(noteId)
        } catch (bre: BadRequestException) {
            NoteResponse.failed(bre.message)
        }
    }

    suspend fun updateNote(user: User, noteId: String, note: NoteRequest): NoteResponse {
        return try {
            val noteTitle = note.title.trim()
            val noteText = note.body.trim()

            validateNoteOrThrowException(noteTitle, noteText)
            checkNoteExistsOrThrowException(noteId)
            checkOwnerOrThrowException(user.id, noteId)

            val id = noteDao.update(noteId, noteTitle, noteText)
            NoteResponse.success(id)
        } catch (uae: UnauthorizedActivityException) {
            NoteResponse.unauthorized(uae.message)
        } catch (bre: BadRequestException) {
            NoteResponse.failed(bre.message)
        } catch (nfe: NoteNotFoundException) {
            NoteResponse.notFound(nfe.message)
        }
    }

    suspend fun deleteNote(user: User, noteId: String): NoteResponse {
        return try {
            checkNoteExistsOrThrowException(noteId)
            checkOwnerOrThrowException(user.id, noteId)

            if (noteDao.deleteById(noteId)) {
                NoteResponse.success(noteId)
            } else {
                NoteResponse.failed("Error Occurred")
            }
        } catch (uae: UnauthorizedActivityException) {
            NoteResponse.unauthorized(uae.message)
        } catch (bre: BadRequestException) {
            NoteResponse.failed(bre.message)
        } catch (nfe: NoteNotFoundException) {
            NoteResponse.notFound(nfe.message)
        }
    }

    suspend fun updateNotePin(user: User, noteId: String, pinRequest: PinRequest): NoteResponse {
        return try {
            checkNoteExistsOrThrowException(noteId)
            checkOwnerOrThrowException(user.id, noteId)
            val id = noteDao.updateNotePinById(noteId, pinRequest.isPinned)
            NoteResponse.success(id)
        } catch (uae: UnauthorizedActivityException) {
            NoteResponse.unauthorized(uae.message)
        } catch (bre: BadRequestException) {
            NoteResponse.failed(bre.message)
        } catch (nfe: NoteNotFoundException) {
            NoteResponse.notFound(nfe.message)
        }
    }

    private suspend fun checkNoteExistsOrThrowException(noteId: String) {
        if (!noteDao.isNoteExists(noteId)) {
            throw NoteNotFoundException("Note not exist with ID '$noteId'")
        }
    }

    private suspend fun checkOwnerOrThrowException(userId: String, noteId: String) {
        if (!noteDao.isNoteOwnedByUser(noteId, userId)) {
            throw UnauthorizedActivityException("Access denied")
        }
    }

    private fun validateNoteOrThrowException(title: String, note: String) {
        val message = when {
            (title.isBlank() or note.isBlank()) -> "Title and Note should not be blank"
            (title.length !in (4..30)) -> "Title should be of min 4 and max 30 character in length"
            else -> return
        }

        throw BadRequestException(message)
    }
}