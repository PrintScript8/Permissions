package austral.ingsis.permissions.service

import austral.ingsis.permissions.factory.UserFactory
import austral.ingsis.permissions.model.UserSnippets
import austral.ingsis.permissions.repository.UserRepositoryInterface
import io.micrometer.core.instrument.MeterRegistry
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
        val users = listOf(UserSnippets())
        every { userRepository.findAll() } returns users

        val result = userService.findAllUsers()

        assertEquals(users, result)
        verify(exactly = 1) { userRepository.findAll() }
    }

    @Test
    fun findUserTest() {
        val user = Optional.of(UserSnippets())
        every { userRepository.findById(1) } returns user

        val result = userService.findUserById(1)

        assertEquals(user.get(), result)
        verify(exactly = 1) { userRepository.findById(1) }
    }

    @Test
    fun saveUserTest() {
        val inputUser = UserSnippets()
        val savedUser = UserSnippets()
        every { userRepository.save(inputUser) } returns savedUser

        val result = userService.saveUser()

        assertEquals(savedUser, result)
        verify(exactly = 1) { userRepository.save(inputUser) }
    }

    @Test
    fun updateUserTest() {
        val inputUser = UserSnippets(1L, emptyList(), emptyList())
        val savedUser = UserSnippets(1L, emptyList(), emptyList())
        every { userRepository.existsById(1) } returns true
        every { userRepository.save(inputUser.copy(id = savedUser.id)) } returns
            UserSnippets(1L, emptyList(), emptyList())

        val result =
            userService.updateUser(
                1,
                UserSnippets(1L, emptyList(), emptyList()),
            )

        assertEquals(inputUser, result)
        verify(exactly = 1) { userRepository.save(inputUser) }
    }
}
