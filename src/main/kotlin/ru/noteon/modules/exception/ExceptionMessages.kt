package ru.noteon.modules.exception

object ExceptionMessages {
    const val MESSAGE_MISSING_CREDENTIALS = "Отсутствует требуемый 'email' или 'password'."
    const val MESSAGE_MISSING_NOTE_DETAILS = "Отсутствует требуемый 'title', 'note' или 'folderId'."

    const val MESSAGE_MISSING_FOLDER_DETAILS = "Отсутствует требуемый 'folderName'."

    const val MESSAGE_MISSING_REFRESH_TOKEN = "Отсутствует требуемый 'refreshToken'."
    const val MESSAGE_REFRESH_TOKEN_INVALID = "Refresh token недействителен."
    const val MESSAGE_REFRESH_TOKEN_EXPIRED_EXCEPTION = "Refresh token недействителен."

    const val MESSAGE_ACCESS_DENIED = "Доступ запрещен!"
    const val MESSAGE_FAILED = "Что-то пошло не так!"

    const val MESSAGE_MISSING_PIN_DETAILS = "Отсутствует требуемый 'isPinned'."
}