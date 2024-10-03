package austral.ingsis.permissions.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class CodeUser(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    val email: String,
    val password: String
)

// no-argument constructor is necessary for JPA to function correctly
{
    constructor() : this(0, "", "", "")
}
