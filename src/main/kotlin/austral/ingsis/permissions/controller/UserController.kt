package austral.ingsis.permissions.controller

import austral.ingsis.permissions.model.UserSnippets
import austral.ingsis.permissions.service.UserService
import jakarta.servlet.http.HttpServletRequest
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    @Autowired private val userService: UserService,
) {
    private val logger = LogManager.getLogger(UserController::class.java)

    @GetMapping
    fun getAllUsers(
        @RequestParam name: String?,
        @RequestParam page: Int,
        @RequestParam pageSize: Int,
        request: HttpServletRequest,
    ): ResponseEntity<Map<String, Any>> {
        val userId = request.getHeader("id").toLong()
        val users = userService.findAllUsers(name, page, pageSize)
        val paginatedResponse =
            mapOf(
                "page" to page,
                "pageSize" to pageSize,
                "count" to users.size,
                "users" to users,
            )
        return ResponseEntity.ok(paginatedResponse)
    }

    @GetMapping("/{id}")
    fun getUserById(
        @PathVariable id: Long,
        request: HttpServletRequest,
    ): UserSnippets? {
        val userId = request.getHeader("id").toLong()
        return userService.findUserById(id)
    }

    @PostMapping
    fun createUser(
        request: HttpServletRequest,
        @RequestParam name: String,
    ): ResponseEntity<UserSnippets> {
        val userId = request.getHeader("id").toLong()
        val user = userService.saveUser(name)
        return ResponseEntity.ok(user)
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @RequestBody codeUser: UserSnippets,
        request: HttpServletRequest,
    ): UserSnippets? {
        val userId = request.getHeader("id").toLong()
        return userService.updateUser(id, codeUser)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(
        @PathVariable id: Long,
        request: HttpServletRequest,
    ) {
        val userId = request.getHeader("id").toLong()
        return userService.deleteUser(id)
    }

    @GetMapping("/snippets/{id}")
    fun getAllSnippets(
        @PathVariable id: Long,
        request: HttpServletRequest,
    ): List<Long> {
        val userId = request.getHeader("id").toLong()
        val snippets = userService.findUserById(id)?.owner ?: emptyList()
        logger.info("User with id $userId has snippets $snippets")
        return snippets
    }

    @PutMapping("/snippets/{snippetId}")
    fun addSnippet(
        @PathVariable snippetId: Long,
        request: HttpServletRequest,
    ): ResponseEntity<Void> {
        val userId = request.getHeader("id").toLong()
        val user: UserSnippets = userService.findUserById(userId) ?: return ResponseEntity.notFound().build()
        if (!user.owner.contains(snippetId)) {
            user.owner = user.owner.plus(snippetId)
            userService.updateUser(user.id, user)
        }
        return ResponseEntity.ok().build()
    }

    @SuppressWarnings("ReturnCount")
    @PutMapping("/snippets/share")
    fun shareSnippet(
        @RequestBody shareSnippetRequest: ShareSnippetRequest,
        request: HttpServletRequest,
    ): ResponseEntity<Void> {
        val userId = request.getHeader("id").toLong()
        if (userId == shareSnippetRequest.id) return ResponseEntity.badRequest().build()
        val user: UserSnippets =
            userService.findUserById(shareSnippetRequest.id)
                ?: return ResponseEntity.notFound().build()
        if (!user.collaborator.contains(shareSnippetRequest.snippetId)) {
            user.collaborator = user.collaborator.plus(shareSnippetRequest.snippetId)
            userService.updateUser(user.id, user)
        }
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/snippets/{id}/{snippetId}")
    fun removeSnippet(
        @PathVariable id: Long,
        @PathVariable snippetId: Long,
        request: HttpServletRequest,
    ): ResponseEntity<Void> {
        val userId = request.getHeader("id").toLong()
        val user = userService.findUserById(id) ?: return ResponseEntity.notFound().build()
        user.owner = user.owner.minus(snippetId)
        userService.updateUser(user.id, user)
        return ResponseEntity.ok().build()
    }
}

data class ShareSnippetRequest(
    val snippetId: Long,
    val id: Long,
)

/**
 * All methods in this class are called by "/user..."
 * To specify which one the following are used: put, get, delete etc.
 * Also, arguments like id can be passed
 */
