package austral.ingsis.permissions.repository

import austral.ingsis.permissions.model.UserSnippets
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepositoryInterface : JpaRepository<UserSnippets, Long>
