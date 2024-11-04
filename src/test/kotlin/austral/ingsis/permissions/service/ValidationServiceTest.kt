package austral.ingsis.permissions.service

import austral.ingsis.permissions.model.UserSnippets
import austral.ingsis.permissions.repository.UserRepositoryInterface
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.util.Optional

class ValidationServiceTest {
    private lateinit var validationService: ValidationService
    private lateinit var userRepository: UserRepositoryInterface

    @BeforeEach
    fun setUp() {
        userRepository = mock(UserRepositoryInterface::class.java)
        validationService = ValidationService(userRepository)
    }

    @Test
    fun `test canModify returns true when user is owner`() {
        val userId = 1L
        val snippetId = 1L
        val userSnippets = UserSnippets(owner = listOf(snippetId), collaborator = emptyList())

        `when`(userRepository.findById(userId)).thenReturn(Optional.of(userSnippets))

        val result = validationService.canModify(userId, snippetId)
        assertTrue(result)
    }

    @Test
    fun `test canModify returns false when user is not owner`() {
        val userId = 1L
        val snippetId = 1L
        val userSnippets = UserSnippets(owner = emptyList(), collaborator = emptyList())

        `when`(userRepository.findById(userId)).thenReturn(Optional.of(userSnippets))

        val result = validationService.canModify(userId, snippetId)
        assertFalse(result)
    }

    @Test
    fun `test canRead returns true when user is owner or collaborator`() {
        val userId = 1L
        val snippetId = 1L
        val userSnippets = UserSnippets(owner = listOf(snippetId), collaborator = listOf(snippetId))

        `when`(userRepository.findById(userId)).thenReturn(Optional.of(userSnippets))

        val result = validationService.canRead(userId, snippetId)
        assertTrue(result)
    }

    @Test
    fun `test canRead returns false when user is neither owner nor collaborator`() {
        val userId = 1L
        val snippetId = 1L
        val userSnippets = UserSnippets(owner = emptyList(), collaborator = emptyList())

        `when`(userRepository.findById(userId)).thenReturn(Optional.of(userSnippets))

        val result = validationService.canRead(userId, snippetId)
        assertFalse(result)
    }

    @Test
    fun `test exists returns true when user exists`() {
        val userId = 1L

        `when`(userRepository.existsById(userId)).thenReturn(true)

        val result = validationService.exists(userId)
        assertTrue(result)
    }

    @Test
    fun `test exists returns false when user does not exist`() {
        val userId = 1L

        `when`(userRepository.existsById(userId)).thenReturn(false)

        val result = validationService.exists(userId)
        assertFalse(result)
    }

    @Test
    fun `test canDelete returns true when user is owner`() {
        val userId = 1L
        val snippetId = 1L
        val userSnippets = UserSnippets(owner = listOf(snippetId), collaborator = emptyList())

        `when`(userRepository.findById(userId)).thenReturn(Optional.of(userSnippets))

        val result = validationService.canDelete(userId, snippetId)
        assertTrue(result)
    }

    @Test
    fun `test canDelete returns false when user is not owner`() {
        val userId = 1L
        val snippetId = 1L
        val userSnippets = UserSnippets(owner = emptyList(), collaborator = emptyList())

        `when`(userRepository.findById(userId)).thenReturn(Optional.of(userSnippets))

        val result = validationService.canDelete(userId, snippetId)
        assertFalse(result)
    }
}
