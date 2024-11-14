package austral.ingsis.permissions.factory

import austral.ingsis.permissions.model.UserSnippets
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Component

@Component
class UserFactory {
    private val logger = LogManager.getLogger(UserFactory::class.java)

    fun buildUser(
        id: String,
        name: String,
        owner: List<Long>,
        collaborator: List<Long>,
    ): UserSnippets {
        val userSnippets = UserSnippets(id, name, owner, collaborator)
        logger.info("User $name created")
        return userSnippets
    }
}
