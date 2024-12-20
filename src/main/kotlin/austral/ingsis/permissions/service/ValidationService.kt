package austral.ingsis.permissions.service

import austral.ingsis.permissions.model.UserSnippets
import austral.ingsis.permissions.repository.UserRepositoryInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ValidationService(
    @Autowired private val userRepository: UserRepositoryInterface,
) {
    fun canModify(
        userId: String,
        snippetId: Long,
    ): Boolean {
        val relation: UserSnippets = userRepository.findById(userId).orElse(null)
        return relation.owner.contains(snippetId)
    }

    fun canRead(
        userId: String,
        snippetId: Long,
    ): Boolean {
        val relation: UserSnippets = userRepository.findById(userId).orElse(null)
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
        return relation.owner.contains(snippetId)
    }
}
