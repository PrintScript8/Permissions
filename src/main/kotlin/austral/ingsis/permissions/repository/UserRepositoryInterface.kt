package austral.ingsis.permissions.repository

import austral.ingsis.permissions.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepositoryInterface : JpaRepository<User, Long>