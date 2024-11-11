package austral.ingsis.permissions.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class UserSnippetsTest {
    @Test
    fun `test UserSnippets default constructor`() {
        val userSnippets = UserSnippets()
        assertEquals(0, userSnippets.id)
        assertEquals("", userSnippets.name)
        assertTrue(userSnippets.owner.isEmpty())
        assertTrue(userSnippets.collaborator.isEmpty())
    }

    @Test
    fun `test UserSnippets parameterized constructor`() {
        val ownerIds = listOf(1L, 2L)
        val collaboratorIds = listOf(3L, 4L)
        val userSnippets = UserSnippets(1, "Test User", ownerIds, collaboratorIds)
        assertEquals(1, userSnippets.id)
        assertEquals("Test User", userSnippets.name)
        assertEquals(ownerIds, userSnippets.owner)
        assertEquals(collaboratorIds, userSnippets.collaborator)
    }

    @Test
    fun `test UserSnippets owner modification`() {
        val userSnippets = UserSnippets(1, "Test User", listOf(1L), listOf(2L))
        val newOwnerIds = listOf(3L, 4L)
        userSnippets.owner = newOwnerIds
        assertEquals(newOwnerIds, userSnippets.owner)
    }

    @Test
    fun `test UserSnippets collaborator modification`() {
        val userSnippets = UserSnippets(1, "Test User", listOf(1L), listOf(2L))
        val newCollaboratorIds = listOf(5L, 6L)
        userSnippets.collaborator = newCollaboratorIds
        assertEquals(newCollaboratorIds, userSnippets.collaborator)
    }
}
