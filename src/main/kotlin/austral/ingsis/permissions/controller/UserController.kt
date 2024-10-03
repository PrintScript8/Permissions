package austral.ingsis.permissions.controller

import austral.ingsis.permissions.model.CodeUser
import austral.ingsis.permissions.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    @Autowired private val userService: UserService
) {

    @GetMapping
    fun getAllUsers(): List<CodeUser>{
        return userService.findAllUsers()
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): CodeUser?{
        return userService.findUserById(id)
    }

    @PostMapping
    fun createUser(@RequestBody codeUser: CodeUser): CodeUser {
        return userService.saveUser(codeUser.name, codeUser.email, codeUser.password)
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody codeUser: CodeUser): CodeUser?{
        return userService.updateUser(id, codeUser)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long){
        return userService.deleteUser(id)
    }
}

/**
 * All methods in this class are called by "/user..."
 * To specify which one the following are used: put, get, delete etc.
 * Also, arguments like id can be passed
 */