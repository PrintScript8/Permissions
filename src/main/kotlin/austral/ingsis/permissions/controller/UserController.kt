package austral.ingsis.permissions.controller

import austral.ingsis.permissions.model.UserSnippets
import austral.ingsis.permissions.service.AuthService
import austral.ingsis.permissions.service.UserService
import com.newrelic.api.agent.Trace
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.nio.file.AccessDeniedException

@RestController
@RequestMapping("/users")
class UserController(
    @Autowired private val userService: UserService,
    @Autowired private val authService: AuthService,
) {
    private fun getIdByToken(token: String): String {
        logger.info("Validating token")
        val id: String? = authService.validateToken(token)
        if (id != null) {
            logger.info("Token validated successfully")
            return id
        }
        // error, not authenticated
        logger.error("Could not validate user by it's token")
        throw AccessDeniedException("Could not validate user by it's token")
    }

    private val logger = LogManager.getLogger(UserController::class.java)

    @GetMapping("/all")
    @Trace(dispatcher = true)
    fun getAllUsers(
        @RequestParam name: String?,
        @RequestParam page: Int,
        @RequestParam pageSize: Int,
        @RequestHeader("Authorization") token: String,
    ): ResponseEntity<Map<String, Any>> {
        val userId = getIdByToken(token)

        logger.info("Request to get all users. UserId: $userId, Page: $page, PageSize: $pageSize")

        val users = userService.findAllUsers(name, page, pageSize)
        val paginatedResponse =
            mapOf(
                "page" to page,
                "pageSize" to pageSize,
                "count" to users.size,
                "users" to users,
            )
        logger.info("Successfully fetched ${users.size} users for userId $userId.")

        return ResponseEntity.ok(paginatedResponse)
    }

    @GetMapping
    @Trace(dispatcher = true)
    fun getUserById(
        @RequestHeader("Authorization") token: String,
    ): UserSnippets? {
        val userId = getIdByToken(token)

        logger.info("Request to get user by ID. UserId: $userId")
        val user = userService.findUserById(userId)

        logger.info("Returning user details for userId: $userId.")
        return user
    }

    @PostMapping
    @Trace(dispatcher = true)
    fun createUser(
        @RequestHeader("Authorization") token: String,
        @RequestHeader("Name") name: String,
    ): ResponseEntity<UserSnippets> {
        val id: String = getIdByToken(token)

        val user = userService.saveUser(id, name)
        logger.info("User with id $id and name $name created successfully")

        return ResponseEntity.ok(user)
    }

    @PutMapping
    @Trace(dispatcher = true)
    fun updateUser(
        @RequestBody codeUser: UserSnippets,
        @RequestHeader("Authorization") token: String,
    ): UserSnippets? {
        val userId = getIdByToken(token)
        logger.info("Request to update user with id $userId")

        val updatedUser = userService.updateUser(userId, codeUser)
        logger.info("User with id $userId updated successfully")

        return updatedUser
    }

    @DeleteMapping
    @Trace(dispatcher = true)
    fun deleteUser(
        @RequestHeader("Authorization") token: String,
    ) {
        val userId = getIdByToken(token)
        logger.info("Request to delete user with id $userId")

        val deletedUser = userService.deleteUser(userId)
        logger.info("User with id $userId deleted successfully")

        return deletedUser
    }

    @GetMapping("/snippets")
    @Trace(dispatcher = true)
    fun getAllSnippets(
        @RequestHeader("Authorization") token: String,
    ): List<Long> {
        val userId = getIdByToken(token)
        logger.info("Request to get all snippets for user with id $userId")

        val snippets = userService.findUserById(userId)?.owner ?: emptyList()
        logger.info("User with id $userId has snippets $snippets")
        return snippets
    }

    @PutMapping("/snippets/{snippetId}")
    @Trace(dispatcher = true)
    fun addSnippet(
        @PathVariable snippetId: Long,
        @RequestHeader("Authorization") token: String,
    ): ResponseEntity<Void> {
        val userId = getIdByToken(token)
        logger.info("Request to add snippet with id $snippetId to user with id $userId")

        val user: UserSnippets = userService.findUserById(userId) ?: return ResponseEntity.notFound().build()
        logger.info("User with id $userId found")

        if (!user.owner.contains(snippetId)) {
            user.owner = user.owner.plus(snippetId)
            userService.updateUser(user.id, user)
            logger.info("Snippet with id $snippetId added to user with id $userId")
        } else {
            logger.info("Snippet with id $snippetId already exists for user with id $userId")
        }
        return ResponseEntity.ok().build()
    }

    @SuppressWarnings("ReturnCount")
    @PutMapping("/snippets/share")
    @Trace(dispatcher = true)
    fun shareSnippet(
        @RequestBody shareSnippetRequest: ShareSnippetRequest,
        @RequestHeader("Authorization") token: String,
    ): ResponseEntity<Void> {
        val userId = getIdByToken(token)
        logger.info(
            "Request to share snippet with id " +
                "${shareSnippetRequest.snippetId} to user " +
                "with id ${shareSnippetRequest.id}",
        )

        if (userId == shareSnippetRequest.id) {
            logger.error("User with id $userId cannot share snippet with id ${shareSnippetRequest.snippetId} to itself")
            return ResponseEntity.badRequest().build()
        }
        val user: UserSnippets =
            userService.findUserById(shareSnippetRequest.id)
                ?: run {
                    logger.error("User with id ${shareSnippetRequest.id} not found")
                    return ResponseEntity.notFound().build()
                }

        if (!user.collaborator.contains(shareSnippetRequest.snippetId)) {
            user.collaborator = user.collaborator.plus(shareSnippetRequest.snippetId)
            userService.updateUser(user.id, user)
            logger.info(
                "Snippet with id ${shareSnippetRequest.snippetId} " +
                    "shared to user with id ${shareSnippetRequest.id}",
            )
        } else {
            logger.info(
                "Snippet with id ${shareSnippetRequest.snippetId} " +
                    "already shared to user with id ${shareSnippetRequest.id}",
            )
        }
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/snippets/{snippetId}")
    @Trace
    fun removeSnippet(
        @PathVariable snippetId: Long,
        @RequestHeader("Authorization") token: String,
    ): ResponseEntity<Void> {
        val userId = getIdByToken(token)
        logger.info("Request to remove snippet with id $snippetId from user with id $userId")

        val user = userService.findUserById(userId) ?: return ResponseEntity.notFound().build()
        user.owner = user.owner.minus(snippetId)
        userService.updateUser(user.id, user)
        logger.info("Snippet with id $snippetId removed from user with id $userId")
        return ResponseEntity.ok().build()
    }
}

data class ShareSnippetRequest(
    val snippetId: Long,
    val id: String,
)

/**
 * All methods in this class are called by "/user..."
 * To specify which one the following are used: put, get, delete etc.
 * Also, arguments like id can be passed
 */
