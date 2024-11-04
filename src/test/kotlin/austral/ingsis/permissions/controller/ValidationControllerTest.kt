package austral.ingsis.permissions.controller

import austral.ingsis.permissions.service.ValidationService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

@ExtendWith(MockitoExtension::class)
class ValidationControllerTest {
    @Mock
    private lateinit var validationService: ValidationService

    @InjectMocks
    private lateinit var validationController: ValidationController

    @Test
    fun `createSnippet should return true when user exists`() {
        val userId = 1L
        `when`(validationService.exists(userId)).thenReturn(true)
        val response: ResponseEntity<Boolean> = validationController.createSnippet(userId)
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body == true)
    }

    @Test
    fun `createSnippet should return false when user does not exist`() {
        val userId = 1L
        `when`(validationService.exists(userId)).thenReturn(false)
        val response: ResponseEntity<Boolean> = validationController.createSnippet(userId)
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body == false)
    }

    @Test
    fun `editSnippet should return true when user can modify snippet`() {
        val userId = 1L
        val snippetId = 1L
        `when`(validationService.canModify(userId, snippetId)).thenReturn(true)
        val response: ResponseEntity<Boolean> = validationController.editSnippet(userId, snippetId)
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body == true)
    }

    @Test
    fun `editSnippet should return false when user cannot modify snippet`() {
        val userId = 1L
        val snippetId = 1L
        `when`(validationService.canModify(userId, snippetId)).thenReturn(false)
        val response: ResponseEntity<Boolean> = validationController.editSnippet(userId, snippetId)
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body == false)
    }

    @Test
    fun `readSnippet should return true when user can read snippet`() {
        val userId = 1L
        val snippetId = 1L
        `when`(validationService.canRead(userId, snippetId)).thenReturn(true)
        val response: ResponseEntity<Boolean> = validationController.readSnippet(userId, snippetId)
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body == true)
    }

    @Test
    fun `readSnippet should return false when user cannot read snippet`() {
        val userId = 1L
        val snippetId = 1L
        `when`(validationService.canRead(userId, snippetId)).thenReturn(false)
        val response: ResponseEntity<Boolean> = validationController.readSnippet(userId, snippetId)
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body == false)
    }

    @Test
    fun `deleteSnippet should return true when user can delete snippet`() {
        val userId = 1L
        val snippetId = 1L
        `when`(validationService.canDelete(userId, snippetId)).thenReturn(true)
        val response: ResponseEntity<Boolean> = validationController.deleteSnippet(userId, snippetId)
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body == true)
    }

    @Test
    fun `deleteSnippet should return false when user cannot delete snippet`() {
        val userId = 1L
        val snippetId = 1L
        `when`(validationService.canDelete(userId, snippetId)).thenReturn(false)
        val response: ResponseEntity<Boolean> = validationController.deleteSnippet(userId, snippetId)
        assert(response.statusCode == HttpStatus.OK)
        assert(response.body == false)
    }
}
