package austral.ingsis.permissions.factory

import austral.ingsis.permissions.model.User
import org.springframework.stereotype.Component

@Component
class UserFactory {

    fun buildUser(username: String, email: String, password: String): User{
        return User(name = username, email = email, password = password)
    }

}