package austral.ingsis.permissions.factory

import austral.ingsis.permissions.model.UserSnippets
import org.springframework.stereotype.Component

@Component
class UserFactory {
    fun buildUser(
        id: String,
        name: String,
        owner: List<Long>,
        collaborator: List<Long>,
    ): UserSnippets {
        return UserSnippets(id, name, owner, collaborator)
    }
}
