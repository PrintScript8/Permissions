package austral.ingsis.permissions.controller

import austral.ingsis.permissions.model.CodeUser
import austral.ingsis.permissions.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    @Autowired private val userService: UserService,
) {
    @GetMapping
    fun getAllUsers(): List<CodeUser> {
        return userService.findAllUsers()
    }

    @GetMapping("/{id}")
    fun getUserById(
        @PathVariable id: Long,
    ): CodeUser? {
        return userService.findUserById(id)
    }

    @PostMapping
    fun createUser(
        @RequestBody codeUser: CodeUser,
    ): CodeUser {
        return userService.saveUser(
            codeUser.name,
            codeUser.email,
            codeUser.password,
        )
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @RequestBody codeUser: CodeUser,
    ): CodeUser? {
        return userService.updateUser(id, codeUser)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(
        @PathVariable id: Long,
    ) {
        return userService.deleteUser(id)
    }

    @GetMapping("/snippets/{id}")
    fun getAllSnippets(
        @PathVariable id: Long,
    ): List<Long> {
        return userService.findUserById(id)?.snippets ?: emptyList()
    }

    @PutMapping("/snippets/{id}/{snippetId}")
    fun addSnippet(
        @PathVariable id: Long,
        @PathVariable snippetId: Long,
    ): ResponseEntity<Void> {
        val user: CodeUser = userService.findUserById(id) ?: return ResponseEntity.notFound().build()
        if (!user.snippets.contains(snippetId)) {
            user.snippets = user.snippets.plus(snippetId)
            userService.updateUser(user.id, user)
        }
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/snippets/{id}/{snippetId}")
    fun removeSnippet(
        @PathVariable id: Long,
        @PathVariable snippetId: Long,
    ): ResponseEntity<Void> {
        val user = userService.findUserById(id) ?: return ResponseEntity.notFound().build()
        user.snippets = user.snippets.minus(snippetId)
        userService.updateUser(user.id, user)
        return ResponseEntity.ok().build()
    }
}

/**
 * All methods in this class are called by "/user..."
 * To specify which one the following are used: put, get, delete etc.
 * Also, arguments like id can be passed
 */
