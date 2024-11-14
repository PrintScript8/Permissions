package austral.ingsis.permissions.service

import austral.ingsis.permissions.model.UserSnippets
import austral.ingsis.permissions.repository.UserRepositoryInterface
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ValidationService(
    @Autowired private val userRepository: UserRepositoryInterface,
) {
    private val logger = LogManager.getLogger(ValidationService::class.java)

    private fun log(userSnippets: UserSnippets) {
        logger.info("UserSnippets: $userSnippets")
    }

    fun canModify(
        userId: String,
        snippetId: Long,
    ): Boolean {
        val relation: UserSnippets = userRepository.findById(userId).orElse(null)
        log(relation)
        return relation.owner.contains(snippetId)
    }

    fun canRead(
        userId: String,
        snippetId: Long,
    ): Boolean {
        val relation: UserSnippets = userRepository.findById(userId).orElse(null)
        log(relation)
        return relation.owner.contains(snippetId) || relation.collaborator.contains(snippetId)
    }

    fun exists(userId: String): Boolean {
        return userRepository.existsById(userId)
    }

    fun canDelete(
        userId: String,
        snippetId: Long,
    ): Boolean {
        val relation: UserSnippets = userRepository.findById(userId).orElse(null)
        log(relation)
        return relation.owner.contains(snippetId)
    }
}
