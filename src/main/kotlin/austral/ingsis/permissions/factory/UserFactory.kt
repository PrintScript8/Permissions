package austral.ingsis.permissions.factory

import austral.ingsis.permissions.model.UserSnippets
import org.springframework.stereotype.Component

@Component
class UserFactory {
    fun buildUser(
        owner: List<Long>,
        collaborator: List<Long>,
    ): UserSnippets {
        return UserSnippets(0L, owner, collaborator)
    }
}
