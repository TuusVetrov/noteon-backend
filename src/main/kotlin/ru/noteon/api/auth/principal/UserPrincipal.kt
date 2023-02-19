package ru.noteon.api.auth.principal

import io.ktor.server.auth.*
import ru.noteon.data.model.User

class UserPrincipal(val user: User) : Principal