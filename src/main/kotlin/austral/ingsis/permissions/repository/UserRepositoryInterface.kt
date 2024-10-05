package austral.ingsis.permissions.repository

import austral.ingsis.permissions.model.CodeUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepositoryInterface : JpaRepository<CodeUser, Long>
