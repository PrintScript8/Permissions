package austral.ingsis.permissions

import austral.ingsis.permissions.factory.UserFactory
import austral.ingsis.permissions.model.CodeUser
import austral.ingsis.permissions.repository.UserRepositoryInterface
import austral.ingsis.permissions.service.UserService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.Optional

class UserServiceTests {
    private val userRepository = mockk<UserRepositoryInterface>()
    private val userFactory = UserFactory()
    private val userService = UserService(userRepository, userFactory)

    @Test
    fun findAllTest() {
        val users = listOf(CodeUser(1, "john", "noOne@mail.com", "1234"))
        every { userRepository.findAll() } returns users

        val result = userService.findAllUsers()

        assertEquals(users, result)
        verify(exactly = 1) { userRepository.findAll() }
    }

    @Test
    fun findUserTest() {
        val user = Optional.of(CodeUser(1, "john", "noOne@mail.com", "1234"))
        every { userRepository.findById(1) } returns user

        val result = userService.findUserById(1)

        assertEquals(user.get(), result)
        verify(exactly = 1) { userRepository.findById(1) }
    }

    @Test
    fun saveUserTest() {
        val inputUser = CodeUser(name = "john", email = "noOne@mail.com", password = "1234")
        val savedUser = CodeUser(1, "john", "noOne@mail.com", "1234")
        every { userRepository.save(inputUser) } returns savedUser

        val result = userService.saveUser("john", "noOne@mail.com", "1234")

        assertEquals(savedUser, result)
        verify(exactly = 1) { userRepository.save(inputUser) }
    }

    @Test
    fun updateUserTest() {
        val inputUser = CodeUser(1, "john doe", "noOne@mail.com", "1234")
        val savedUser = CodeUser(1, "john", "noOne@mail.com", "1234")
        every { userRepository.existsById(1) } returns true
        every { userRepository.save(inputUser.copy(id = savedUser.id)) } returns
            CodeUser(
                1,
                "john doe",
                "noOne@mail.com",
                "1234",
            )

        val result =
            userService.updateUser(
                1,
                CodeUser(1, "john doe", "noOne@mail.com", "1234"),
            )

        assertEquals(inputUser, result)
        verify(exactly = 1) { userRepository.save(inputUser) }
    }
}
