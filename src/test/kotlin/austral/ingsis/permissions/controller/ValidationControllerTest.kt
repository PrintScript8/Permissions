package austral.ingsis.permissions.controller

import austral.ingsis.permissions.service.ValidationService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ValidationController::class)
class ValidationControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var validationService: ValidationService

    @Test
    fun `test createSnippet when user exists`() {
        val userId = "st-id"
        Mockito.`when`(validationService.exists(userId)).thenReturn(true)

        mockMvc.perform(put("/validate/create/$userId"))
            .andExpect(status().isOk)
    }

    @Test
    fun `test createSnippet when user does not exist`() {
        val userId = "st-id"
        Mockito.`when`(validationService.exists(userId)).thenReturn(false)

        mockMvc.perform(put("/validate/create/$userId"))
            .andExpect(status().isOk)
    }

    @Test
    fun `test editSnippet when user can modify`() {
        val userId = "st-id"
        val snippetId = 1L
        Mockito.`when`(validationService.canModify(userId, snippetId)).thenReturn(true)

        mockMvc.perform(put("/validate/edit/$userId/$snippetId"))
            .andExpect(status().isOk)
    }

    @Test
    fun `test editSnippet when user cannot modify`() {
        val userId = "st-id"
        val snippetId = 1L
        Mockito.`when`(validationService.canModify(userId, snippetId)).thenReturn(false)

        mockMvc.perform(put("/validate/edit/$userId/$snippetId"))
            .andExpect(status().isOk)
    }

    @Test
    fun `test readSnippet when user can read`() {
        val userId = "st-id"
        val snippetId = 1L
        Mockito.`when`(validationService.canRead(userId, snippetId)).thenReturn(true)

        mockMvc.perform(put("/validate/read/$userId/$snippetId"))
            .andExpect(status().isOk)
    }

    @Test
    fun `test readSnippet when user cannot read`() {
        val userId = "st-id"
        val snippetId = 1L
        Mockito.`when`(validationService.canRead(userId, snippetId)).thenReturn(false)

        mockMvc.perform(put("/validate/read/$userId/$snippetId"))
            .andExpect(status().isOk)
    }

    @Test
    fun `test deleteSnippet when user can delete`() {
        val userId = "st-id"
        val snippetId = 1L
        Mockito.`when`(validationService.canDelete(userId, snippetId)).thenReturn(true)

        mockMvc.perform(put("/validate/delete/$userId/$snippetId"))
            .andExpect(status().isOk)
    }

    @Test
    fun `test deleteSnippet when user cannot delete`() {
        val userId = "st-id"
        val snippetId = 1L
        Mockito.`when`(validationService.canDelete(userId, snippetId)).thenReturn(false)

        mockMvc.perform(put("/validate/delete/$userId/$snippetId"))
            .andExpect(status().isOk)
    }
}
