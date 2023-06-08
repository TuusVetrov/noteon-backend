package ru.noteon.modules.users

import ru.noteon.modules.exception.BadRequestException
import ru.noteon.data.dao.token.TokenDaoFacade
import ru.noteon.data.dao.user.UserDaoFacade
import ru.noteon.data.model.UserModel
import ru.noteon.data.model.request.FolderRequest
import ru.noteon.data.model.response.FolderResponse
import ru.noteon.data.model.response.UpdateUserResponse
import ru.noteon.data.model.response.UserResponse
import ru.noteon.modules.exception.NoteNotFoundException
import ru.noteon.modules.exception.UnauthorizedActivityException
import ru.noteon.utils.Encryptor
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileController @Inject constructor(
    private val userDao: UserDaoFacade,
    private val tokenDao: TokenDaoFacade,
    private val encryptor: Encryptor
) {
    suspend fun changeUserData(
        userModel: UserModel,
        newUsername:String,
        newEmail: String,
    ): UpdateUserResponse {
        return try {
            if (!userDao.isEmailAvailable(newEmail)) {
                throw BadRequestException("User with this email already exists")
            }

            val id = userDao.updateUserData(userModel.id,  newUsername, newEmail)
            UpdateUserResponse.success(id)
        } catch (uae: UnauthorizedActivityException) {
            UpdateUserResponse.unauthorized(uae.message)
        } catch (bre: BadRequestException) {
            UpdateUserResponse.failed(bre.message)
        } catch (nfe: NoteNotFoundException) {
            UpdateUserResponse.notFound(nfe.message)
        }
    }

    suspend fun changeUserPassword(
        userModel: UserModel,
        newPassword:String,
    ): UpdateUserResponse {
        return try {
            val id = userDao.updateUserPassword(userModel.id, encryptor.encrypt(newPassword))
            UpdateUserResponse.success(id)
        } catch (uae: UnauthorizedActivityException) {
            UpdateUserResponse.unauthorized(uae.message)
        } catch (bre: BadRequestException) {
            UpdateUserResponse.failed(bre.message)
        } catch (nfe: NoteNotFoundException) {
            UpdateUserResponse.notFound(nfe.message)
        }
    }

    suspend fun getUserById(
        userModel: UserModel,
    ): UserResponse {
        return try {
            val data = userDao.findByUUID(UUID.fromString(userModel.id))
            UserResponse.success(data!!.id, data.username, data.email)
        } catch (uae: UnauthorizedActivityException) {
            UserResponse.unauthorized(uae.message)
        } catch (bre: BadRequestException) {
            UserResponse.failed(bre.message)
        }
    }


    suspend fun logout(refreshToken: String): Boolean {
        return tokenDao.removeToken(refreshToken)
    }
}