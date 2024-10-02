package austral.ingsis.permissions.service

import austral.ingsis.permissions.factory.UserFactory
import austral.ingsis.permissions.model.User
import austral.ingsis.permissions.repository.UserRepositoryInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService(
    @Autowired private val userRepository: UserRepositoryInterface,
    @Autowired private val userFactory: UserFactory
) {

    fun findAllUsers(): List<User>{
        return userRepository.findAll()
    }

    fun findUserById(id: Long): User?{
        return userRepository.findById(id).orElse(null)
    }

    fun saveUser(name: String, email: String, password: String): User{
        val user: User = userFactory.buildUser(name, email, password)
        return userRepository.save(user)
    }

    fun updateUser(id: Long, user: User): User? {
        return if (userRepository.existsById(id)) {
            userRepository.save(user.copy(id = id))
        } else {
            null
        }
    }

    fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }
}
