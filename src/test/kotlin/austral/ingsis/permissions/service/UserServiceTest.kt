package austral.ingsis.permissions.service

import austral.ingsis.permissions.factory.UserFactory
import austral.ingsis.permissions.model.UserSnippets
import austral.ingsis.permissions.repository.UserRepositoryInterface
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.anyString
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.web.client.RestClient

class UserServiceTest {
    private lateinit var userService: UserService
    private lateinit var userRepository: UserRepositoryInterface
    private lateinit var userFactory: UserFactory
    private lateinit var restBuilder: RestClient.Builder

    @BeforeEach
    fun setUp() {
        userRepository = mock(UserRepositoryInterface::class.java)
        userFactory = mock(UserFactory::class.java)
        restBuilder = mock(RestClient.Builder::class.java)
        val restClient = mock(RestClient::class.java)

        `when`(restBuilder.baseUrl(anyString())).thenReturn(restBuilder)
        `when`(restBuilder.build()).thenReturn(restClient)

        userService = UserService(userRepository, userFactory, restBuilder)
    }

    @Test
    fun `test findAllUsers with name`() {
        val name = "test"
        val page = 0
        val pageSize = 10
        val pageable = PageRequest.of(page, pageSize)
        val userSnippets = listOf(UserSnippets("st-id", "testUser", listOf(), listOf()))
        val pageResult: Page<UserSnippets> = PageImpl(userSnippets)

        `when`(userRepository.findByNameContaining(name, pageable)).thenReturn(pageResult)

        val result = userService.findAllUsers(name, page, pageSize)

        assertEquals(userSnippets, result)
    }

    @Test
    fun `test findAllUsers without name`() {
        val page = 0
        val pageSize = 10
        val pageable = PageRequest.of(page, pageSize)
        val userSnippets = listOf(UserSnippets("st-id", "testUser", listOf(), listOf()))
        val pageResult: Page<UserSnippets> = PageImpl(userSnippets)

        `when`(userRepository.findAll(pageable)).thenReturn(pageResult)

        val result = userService.findAllUsers(null, page, pageSize)

        assertEquals(userSnippets, result)
    }

    @Test
    fun `test findUserById`() {
        val id = "st-id"
        val userSnippet = UserSnippets(id, "testUser", listOf(), listOf())

        `when`(userRepository.findById(id)).thenReturn(java.util.Optional.of(userSnippet))

        val result = userService.findUserById(id)

        assertEquals(userSnippet, result)
    }

    @Test
    fun `test updateUser`() {
        val id = "st-id"
        val userSnippet = UserSnippets(id, "testUser", listOf(), listOf())

        `when`(userRepository.existsById(id)).thenReturn(true)
        `when`(userRepository.save(userSnippet)).thenReturn(userSnippet)

        val result = userService.updateUser(id, userSnippet)

        assertEquals(userSnippet, result)
    }

    @Test
    fun `test deleteUser`() {
        val id = "st-id"

        doNothing().`when`(userRepository).deleteById(id)

        userService.deleteUser(id)

        verify(userRepository, times(1)).deleteById(id)
    }
}
