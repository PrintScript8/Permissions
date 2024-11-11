package austral.ingsis.permissions.controller

import austral.ingsis.permissions.model.UserSnippets
import austral.ingsis.permissions.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(UserController::class)
class UserControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userService: UserService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        // Setup mock behavior here if needed
    }

    @Test
    fun `should get all users`() {
        val users = listOf(UserSnippets(1, "John Doe", listOf(), listOf()))
        Mockito.`when`(userService.findAllUsers(null, 0, 10)).thenReturn(users)

        mockMvc.perform(
            get("/users")
                .param("page", "0")
                .param("pageSize", "10")
                .header("id", "1"),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.count").value(users.size))
            .andExpect(jsonPath("$.users[0].name").value("John Doe"))
    }

    @Test
    fun `should get user by id`() {
        val user = UserSnippets(1, "John Doe", listOf(), listOf())
        Mockito.`when`(userService.findUserById(1)).thenReturn(user)

        mockMvc.perform(
            get("/users/1")
                .header("id", "1"),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("John Doe"))
    }

    @Test
    fun `should create user`() {
        val user = UserSnippets(1, "John Doe", listOf(), listOf())
        Mockito.`when`(userService.saveUser("John Doe")).thenReturn(user)

        mockMvc.perform(
            post("/users")
                .param("name", "John Doe")
                .header("id", "1"),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("John Doe"))
    }

    @Test
    fun `should update user`() {
        val user = UserSnippets(1, "John Doe", listOf(), listOf())
        Mockito.`when`(userService.updateUser(1, user)).thenReturn(user)

        mockMvc.perform(
            put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))
                .header("id", "1"),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("John Doe"))
    }

    @Test
    fun `should delete user`() {
        Mockito.doNothing().`when`(userService).deleteUser(1)

        mockMvc.perform(
            delete("/users/1")
                .header("id", "1"),
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `should get all snippets`() {
        val snippets = listOf(1L, 2L, 3L)
        val user = UserSnippets(1, "John Doe", snippets, listOf())
        Mockito.`when`(userService.findUserById(1)).thenReturn(user)

        mockMvc.perform(
            get("/users/snippets/1")
                .header("id", "1"),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0]").value(1L))
            .andExpect(jsonPath("$[1]").value(2L))
            .andExpect(jsonPath("$[2]").value(3L))
    }

    @Test
    fun `should add snippet`() {
        val user = UserSnippets(1, "John Doe", listOf(), listOf())
        Mockito.`when`(userService.findUserById(1)).thenReturn(user)
        Mockito.`when`(userService.updateUser(1, user)).thenReturn(user)

        mockMvc.perform(
            put("/users/snippets/1")
                .header("id", "1"),
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `should share snippet`() {
        val user = UserSnippets(2, "Jane Doe", listOf(), listOf())
        val shareSnippetRequest = ShareSnippetRequest(1, 2)
        Mockito.`when`(userService.findUserById(2)).thenReturn(user)
        Mockito.`when`(userService.updateUser(2, user)).thenReturn(user)

        mockMvc.perform(
            put("/users/snippets/share")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(shareSnippetRequest))
                .header("id", "1"),
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `should remove snippet`() {
        val user = UserSnippets(1, "John Doe", listOf(1L, 2L, 3L), listOf())
        Mockito.`when`(userService.findUserById(1)).thenReturn(user)
        Mockito.`when`(userService.updateUser(1, user)).thenReturn(user)

        mockMvc.perform(
            delete("/users/snippets/1/1")
                .header("id", "1"),
        )
            .andExpect(status().isOk)
    }
}
