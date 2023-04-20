package ru.noteon.api.exception

object ExceptionMessages {
    const val MESSAGE_MISSING_CREDENTIALS = "Required 'email' or 'password' missing."
    const val MESSAGE_MISSING_NOTE_DETAILS = "Required 'title' or 'note' missing."

    const val MESSAGE_MISSING_REFRESH_TOKEN = "Required 'refreshToken' missing."
    const val MESSAGE_REFRESH_TOKEN_INVALID = "Refresh token is invalid."
    const val MESSAGE_REFRESH_TOKEN_EXPIRED_EXCEPTION = "Refresh token is invalid."

    const val MESSAGE_ACCESS_DENIED = "Access Denied!"
    const val MESSAGE_FAILED = "Something went wrong!"

    const val MESSAGE_MISSING_PIN_DETAILS = "Required 'isPinned' missing."
}