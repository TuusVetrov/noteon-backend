package ru.noteon.api.exception

class NoteNotFoundException(override val message: String) : Exception(message)

class BadRequestException(override val message: String) : Exception(message)

class UnauthorizedActivityException(override val message: String) : Exception(message)