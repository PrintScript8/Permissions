package austral.ingsis.permissions

import austral.ingsis.permissions.controller.UserController
import austral.ingsis.permissions.model.CodeUser
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
        val users = listOf(CodeUser(1, "john", "noOne@mail.com", "1234"))
        every { userService.findAllUsers() } returns users

        mockMvc.perform(get("/users"))
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(users)))
    }

    @Test
    fun getUserByIdTest() {
        val user = CodeUser(1, "john", "noOne@mail.com", "1234")
        every { userService.findUserById(1) } returns user

        mockMvc.perform(get("/users/1"))
            .andExpect(status().isOk)
            .andExpect(content().json(jacksonObjectMapper().writeValueAsString(user)))
    }

    @Test
    fun createUserTest() {
        val user = CodeUser(1, "john", "noOne@mail.com", "1234")
        every { userService.saveUser("john", "noOne@mail.com", "1234") } returns user

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
        val user = CodeUser(1, "john", "noOne@mail.com", "1234")
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
}
