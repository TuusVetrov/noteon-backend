package ru.noteon.api.controllers

import ru.noteon.api.exception.BadRequestException
import ru.noteon.api.exception.NoteNotFoundException
import ru.noteon.api.exception.UnauthorizedActivityException
import ru.noteon.data.dao.folder.FolderDaoFacade
import ru.noteon.data.model.UserModel
import ru.noteon.data.model.request.FolderRequest
import ru.noteon.data.model.response.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoldersController @Inject constructor(
    private val folderDao: FolderDaoFacade
) {
    suspend fun getFoldersByUser(userModel: UserModel): FoldersListResponse {
        return try {
            val folders = folderDao.getAllByUser(userModel.id)
            FoldersListResponse.success(folders.map { Folder(it.id, it.folderName) })
        } catch (uae: UnauthorizedActivityException) {
            FoldersListResponse.unauthorized(uae.message)
        }
    }

    suspend fun addFolder(userModel: UserModel, folder: FolderRequest): FolderResponse {
        return try {
            val folderName = folder.folderName.trim()

            validateFolderNameOrThrowException(folderName)

            val folderId = folderDao.add(userModel.id, folderName)
            FolderResponse.success(folderId)
        } catch (bre: BadRequestException) {
            FolderResponse.failed(bre.message)
        }
    }


    suspend fun updateFolder(userModel: UserModel, folderId: String, folder: FolderRequest): FolderResponse {
        return try {
            val folderName = folder.folderName.trim()

            validateFolderNameOrThrowException(folderName)
            checkFolderExistsOrThrowException(folderId)
            checkOwnerFolderOrThrowException(userModel.id, folderId)

            val id = folderDao.update(folderId, folderName)
            FolderResponse.success(id)
        } catch (uae: UnauthorizedActivityException) {
            FolderResponse.unauthorized(uae.message)
        } catch (bre: BadRequestException) {
            FolderResponse.failed(bre.message)
        } catch (nfe: NoteNotFoundException) {
            FolderResponse.notFound(nfe.message)
        }
    }

    suspend fun deleteFolder(userModel: UserModel, folderId: String): NoteResponse {
        return try {
            checkFolderExistsOrThrowException(folderId)
            checkOwnerFolderOrThrowException(userModel.id, folderId)

            if (folderDao.deleteById(folderId)) {
                NoteResponse.success(folderId)
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

    private fun validateFolderNameOrThrowException(folderName: String) {
        val message = when {
            folderName.isBlank() -> "Folder name should not be blank"
            else -> return
        }

        throw BadRequestException(message)
    }

    private suspend fun checkFolderExistsOrThrowException(folderId: String) {
        if (!folderDao.isFolderExists(folderId)) {
            throw NoteNotFoundException("Folder not exist with ID '$folderId'")
        }
    }

    private suspend fun checkOwnerFolderOrThrowException(userId: String, folderId: String) {
        if (!folderDao.isFolderOwnedByUser(folderId, userId)) {
            throw UnauthorizedActivityException("Access denied")
        }
    }
}