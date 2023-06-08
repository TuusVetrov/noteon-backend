package ru.noteon.plugins

import dagger.Lazy
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import ru.noteon.modules.auth.JWTController
import ru.noteon.modules.auth.NoteonJWTController
import ru.noteon.data.model.principal.UserPrincipal
import ru.noteon.data.dao.user.UserDaoFacade
import java.util.*

fun Application.configureAuthentication(
    jwtController: Lazy<JWTController> = appComponent.controllerComponent().jwtController(),
    userDao: Lazy<UserDaoFacade> = appComponent.daoComponent().userDao()
) {
    install(Authentication) {
        jwt() {
            verifier(jwtController.get().verifyAccessToken)
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