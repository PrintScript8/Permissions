package austral.ingsis.permissions.factory

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UserFactoryTest {
    private val userFactory = UserFactory()

    @Test
    fun `test buildUser with valid inputs`() {
        val name = "John Doe"
        val owner = listOf(1L, 2L)
        val collaborator = listOf(3L, 4L)

        val userSnippets = userFactory.buildUser("st-id", name, owner, collaborator)

        assertEquals("st-id", userSnippets.id)
        assertEquals(name, userSnippets.name)
        assertEquals(owner, userSnippets.owner)
        assertEquals(collaborator, userSnippets.collaborator)
    }
}
