package austral.ingsis.permissions.service

import austral.ingsis.permissions.factory.UserFactory
import austral.ingsis.permissions.model.CodeUser
import austral.ingsis.permissions.repository.UserRepositoryInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService(
    @Autowired private val userRepository: UserRepositoryInterface,
    @Autowired private val userFactory: UserFactory,
) {
    fun findAllUsers(): List<CodeUser>  {
        return userRepository.findAll()
    }

    fun findUserById(id: Long): CodeUser?  {
        return userRepository.findById(id).orElse(null)
    }

    fun saveUser(
        name: String,
        email: String,
        password: String,
    ): CodeUser  {
        val codeUser: CodeUser = userFactory.buildUser(name, email, password)
        return userRepository.save(codeUser)
    }

    fun updateUser(
        id: Long,
        codeUser: CodeUser,
    ): CodeUser? {
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
