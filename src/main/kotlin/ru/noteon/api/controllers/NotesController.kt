package ru.noteon.api.controllers

import ru.noteon.api.exception.BadRequestException
import ru.noteon.api.exception.NoteNotFoundException
import ru.noteon.api.exception.UnauthorizedActivityException
import ru.noteon.data.dao.folder.FolderDaoFacade
import ru.noteon.data.model.request.NoteRequest
import ru.noteon.data.model.request.PinRequest
import ru.noteon.data.model.response.NotesListResponse
import ru.noteon.data.dao.note.NoteDaoFacade
import ru.noteon.data.model.UserModel
import ru.noteon.data.model.response.Note
import ru.noteon.data.model.response.NoteResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotesController @Inject constructor(
    private val noteDao: NoteDaoFacade,
    private val folderDao: FolderDaoFacade
) {
    suspend fun getNotesByUser(userModel: UserModel): NotesListResponse {
        return try {
            val notes = noteDao.getAllByUser(userModel.id)

            NotesListResponse.success(notes.map { Note(it.id, it.folder, it.title, it.body, it.created, it.isPinned) })
        } catch (uae: UnauthorizedActivityException) {
            NotesListResponse.unauthorized(uae.message)
        }
    }

    suspend fun getAllFromFolder(userModel: UserModel, folderId: String): NotesListResponse {
        return try {
            checkFolderExistsOrThrowException(folderId)

            val notes = noteDao.getAllFromFolder(userModel.id, folderId)

            NotesListResponse.success(notes.map { Note(it.id, it.folder, it.title, it.body, it.created, it.isPinned) })
        } catch (uae: UnauthorizedActivityException) {
            NotesListResponse.unauthorized(uae.message)
        }
    }

    suspend fun addNote(userModel: UserModel, note: NoteRequest): NoteResponse {
        return try {
            val noteTitle = note.title.trim()
            val noteBody = note.body.trim()
            val folderId = note.folderId.trim()

            validateNoteOrThrowException(noteTitle, noteBody)

            val noteId = noteDao.add(userModel.id, folderId, noteTitle, noteBody)
            NoteResponse.success(noteId)
        } catch (bre: BadRequestException) {
            NoteResponse.failed(bre.message)
        }
    }

    suspend fun updateNote(userModel: UserModel, noteId: String, note: NoteRequest): NoteResponse {
        return try {
            val noteTitle = note.title.trim()
            val noteText = note.body.trim()
            val folderId = note.folderId.trim()

            validateNoteOrThrowException(noteTitle, noteText)
            checkNoteExistsOrThrowException(noteId)
            checkFolderExistsOrThrowException(folderId)
            checkOwnerNoteOrThrowException(userModel.id, noteId)
            checkOwnerFolderOrThrowException(userModel.id, folderId)

            val id = noteDao.update(noteId, folderId, noteTitle, noteText)
            NoteResponse.success(id)
        } catch (uae: UnauthorizedActivityException) {
            NoteResponse.unauthorized(uae.message)
        } catch (bre: BadRequestException) {
            NoteResponse.failed(bre.message)
        } catch (nfe: NoteNotFoundException) {
            NoteResponse.notFound(nfe.message)
        }
    }

    suspend fun deleteNote(userModel: UserModel, noteId: String): NoteResponse {
        return try {
            checkNoteExistsOrThrowException(noteId)
            checkOwnerNoteOrThrowException(userModel.id, noteId)

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

    suspend fun updateNotePin(userModel: UserModel, noteId: String, pinRequest: PinRequest): NoteResponse {
        return try {
            checkNoteExistsOrThrowException(noteId)
            checkOwnerNoteOrThrowException(userModel.id, noteId)
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

    private suspend fun checkFolderExistsOrThrowException(folderId: String) {
        if (!folderDao.isFolderExists(folderId)) {
            throw NoteNotFoundException("Folder not exist with ID '$folderId'")
        }
    }

    private suspend fun checkOwnerNoteOrThrowException(userId: String, noteId: String) {
        if (!noteDao.isNoteOwnedByUser(noteId, userId)) {
            throw UnauthorizedActivityException("Access denied")
        }
    }

    private suspend fun checkOwnerFolderOrThrowException(userId: String, folderId: String) {
        if (!folderDao.isFolderOwnedByUser(folderId, userId)) {
            throw UnauthorizedActivityException("Access denied")
        }
    }

    private fun validateNoteOrThrowException(title: String, note: String) {
        val message = when {
            (title.isBlank() or note.isBlank()) -> "Title and Note should not be blank"
            else -> return
        }

        throw BadRequestException(message)
    }
}