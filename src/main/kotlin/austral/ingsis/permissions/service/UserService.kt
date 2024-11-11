package austral.ingsis.permissions.service

import austral.ingsis.permissions.factory.UserFactory
import austral.ingsis.permissions.model.UserSnippets
import austral.ingsis.permissions.repository.UserRepositoryInterface
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class UserService(
    @Autowired private val userRepository: UserRepositoryInterface,
    @Autowired private val userFactory: UserFactory,
    @Autowired private val restBuilder: RestClient.Builder,
) {
    val snippetClient = restBuilder.baseUrl("http://snippet-service:8080").build()
    private val logger = LogManager.getLogger(UserService::class.java)

    fun findAllUsers(
        name: String?,
        page: Int,
        pageSize: Int,
    ): List<UserSnippets> {
        val pageable = PageRequest.of(page, pageSize)
        return if (!name.isNullOrEmpty()) {
            userRepository.findByNameContaining(name, pageable).content
        } else {
            val users = userRepository.findAll(pageable).content
            logger.info("Users retrieved: $users.findAll(pageable).content")
            users
        }
    }

    fun findUserById(id: Long): UserSnippets? {
        return userRepository.findById(id).orElse(null)
    }

    fun saveUser(name: String): UserSnippets {
        val codeUser: UserSnippets = userFactory.buildUser(name, listOf(), listOf())
        val user = userRepository.save(codeUser)
        snippetClient.put()
            .uri { uriBuilder ->
                uriBuilder.path("/snippets/config/initialize/{userId}")
                    .queryParam("language", "printscript")
                    .build(user.id)
            }
            .retrieve()
        return user
    }

    fun updateUser(
        id: Long,
        codeUser: UserSnippets,
    ): UserSnippets? {
        return if (userRepository.existsById(id)) {
            userRepository.save(codeUser.copy(id = id))
        } else {
            null
        }
    }

    fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }
}

data class Message(
    val language: String,
)
