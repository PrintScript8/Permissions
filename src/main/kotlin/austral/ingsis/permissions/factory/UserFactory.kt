package austral.ingsis.permissions.factory

import austral.ingsis.permissions.model.CodeUser
import org.springframework.stereotype.Component

@Component
class UserFactory {
    fun buildUser(
        username: String,
        email: String,
        password: String,
    ): CodeUser {
        return CodeUser(0L, username, email, password, emptyList())
    }
}
