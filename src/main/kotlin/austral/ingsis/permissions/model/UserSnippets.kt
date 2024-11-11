package austral.ingsis.permissions.model

import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class UserSnippets(
    @Id
    val id: String,
    val name: String,
    @ElementCollection
    var owner: List<Long>,
    @ElementCollection
    var collaborator: List<Long>,
) {
    // no-argument constructor is necessary for JPA to function correctly
    constructor() : this("", "", emptyList(), emptyList())
}
