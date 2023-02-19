package ru.noteon.plugins

import dagger.Lazy
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import ru.noteon.api.auth.JWTController
import ru.noteon.api.auth.NoteonJWTController
import ru.noteon.api.auth.principal.UserPrincipal
import ru.noteon.data.dao.UserDao
import java.util.*

fun Application.configureAuthentication(
    jwtController: Lazy<JWTController> = appComponent.controllerComponent().jwtController(),
    userDao: Lazy<UserDao> = appComponent.daoComponent().userDao()
) {
    install(Authentication) {
        jwt {
            verifier(jwtController.get().verifier)
            validate {

                // Extract userId from token
                val userId = it.payload.getClaim(NoteonJWTController.ClAIM).asString()

                // Return Principle if user exists otherwise null
                val user = userDao.get().findByUUID(UUID.fromString(userId))
                if (user != null) UserPrincipal(user) else null
            }
        }
    }
}