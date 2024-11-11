package austral.ingsis.permissions.repository

import austral.ingsis.permissions.model.UserSnippets
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepositoryInterface : JpaRepository<UserSnippets, Long> {
    fun findByNameContaining(
        name: String,
        pageable: Pageable,
    ): Page<UserSnippets>
}
