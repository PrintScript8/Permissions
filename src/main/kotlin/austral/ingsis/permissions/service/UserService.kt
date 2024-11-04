package austral.ingsis.permissions.service

import austral.ingsis.permissions.factory.UserFactory
import austral.ingsis.permissions.model.UserSnippets
import austral.ingsis.permissions.repository.UserRepositoryInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService(
    @Autowired private val userRepository: UserRepositoryInterface,
    @Autowired private val userFactory: UserFactory,
) {
    fun findAllUsers(): List<UserSnippets> {
        return userRepository.findAll()
    }

    fun findUserById(id: Long): UserSnippets? {
        return userRepository.findById(id).orElse(null)
    }

    fun saveUser(): UserSnippets {
        val codeUser: UserSnippets = userFactory.buildUser(listOf(), listOf())
        return userRepository.save(codeUser)
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
