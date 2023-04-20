package ru.noteon.data.model.principal

import io.ktor.server.auth.*
import ru.noteon.data.model.UserModel

class UserPrincipal(val userModel: UserModel) : Principal