package austral.ingsis.permissions.controller

import austral.ingsis.permissions.model.UserSnippets
import austral.ingsis.permissions.service.UserService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(UserController::class)
class UserControllerTests {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockkBean
    lateinit var userService: UserService

    @Test
    fun getAllUsersTest() {
        val users = listOf(UserSnippets())
        every { userService.findAllUsers() } returns users

        mockMvc.perform(get("/users"))
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(users)))
    }

    @Test
    fun getUserByIdTest() {
        val user = UserSnippets()
        every { userService.findUserById(1) } returns user

        mockMvc.perform(get("/users/1"))
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(user)))
    }

    @Test
    fun createUserTest() {
        val user = UserSnippets()
        every { userService.saveUser() } returns user

        mockMvc.perform(
            post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(user)),
        )
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(user)))
    }

    @Test
    fun updateUserTest() {
        val user = UserSnippets()
        every { userService.updateUser(1, user) } returns user

        mockMvc.perform(
            put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(user)),
        )
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(user)))
    }

    @Test
    fun deleteUserTest() {
        every { userService.deleteUser(1) } returns Unit

        mockMvc.perform(delete("/users/1"))
            .andExpect(status().isOk)
    }

    @Test
    fun getAllSnippetsTest() {
        val snippets = listOf(1L, 2L, 3L)
        val user = UserSnippets(1, listOf(1L, 2L, 3L), emptyList())
        every { userService.findUserById(1) } returns user

        mockMvc.perform(get("/users/snippets/1"))
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(snippets)))
    }

    @Test
    fun addSnippetTest() {
        val user = UserSnippets(1, emptyList(), emptyList())
        every { userService.findUserById(1L) } returns user
        every { userService.updateUser(1L, any()) } returns user.copy()

        mockMvc.perform(
            put("/users/snippets/1/2"),
        )
            .andExpect(status().isOk)
    }

    @Test
    fun removeSnippetTest() {
        val user = UserSnippets(1, listOf(1, 2, 3), emptyList())
        every { userService.findUserById(1) } returns user
        every { userService.updateUser(1, any()) } returns user.copy()

        mockMvc.perform(delete("/users/snippets/1/2"))
            .andExpect(status().isOk)
    }
}
