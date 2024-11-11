package austral.ingsis.permissions.controller

import austral.ingsis.permissions.service.AuthService
import austral.ingsis.permissions.service.ValidationService
import jakarta.servlet.http.HttpServletRequest
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.nio.file.AccessDeniedException

@RestController
@RequestMapping("/validate")
class ValidationController(
    @Autowired private val validationService: ValidationService,
    @Autowired private val authService: AuthService,
) {
    private fun getIdByToken(token: String): String {
        val id: String? = authService.validateToken(token)
        if (id != null) {
            return id
        }
        // error, not authenticated
        throw AccessDeniedException("Could not validate user by it's token")
    }

    private val logger: Logger = LogManager.getLogger(ValidationController::class.java)

    @PutMapping("/create")
    fun createSnippet(request: HttpServletRequest): ResponseEntity<Boolean> {
        val token = request.getHeader("Authorization")
        val userId = getIdByToken(token)

        val exists = validationService.exists(userId)
        if (!exists) {
            logger.error("User with id $userId does not exist")
        } else {
            logger.info("User with id $userId exists")
        }
        return ResponseEntity.ok(exists)
    }

    @PutMapping("/edit/{snippetId}")
    fun editSnippet(
        @PathVariable snippetId: Long,
        request: HttpServletRequest,
    ): ResponseEntity<Boolean> {
        val token = request.getHeader("Authorization")
        val userId = getIdByToken(token)

        val canModify = validationService.canModify(userId, snippetId)
        if (!canModify) {
            logger.error("User with id $userId cannot modify snippet with id $snippetId")
        } else {
            logger.info("User with id $userId can modify snippet with id $snippetId")
        }
        return ResponseEntity.ok(canModify)
    }

    @PutMapping("/read/{snippetId}")
    fun readSnippet(
        @PathVariable snippetId: Long,
        request: HttpServletRequest,
    ): ResponseEntity<Boolean> {
        val token = request.getHeader("Authorization")
        val userId = getIdByToken(token)

        val canRead = validationService.canRead(userId, snippetId)
        if (!canRead) {
            logger.error("User with id $userId cannot read snippet with id $snippetId")
        } else {
            logger.info("User with id $userId can read snippet with id $snippetId")
        }
        return ResponseEntity.ok(canRead)
    }

    @PutMapping("/delete/{snippetId}")
    fun deleteSnippet(
        @PathVariable snippetId: Long,
        request: HttpServletRequest,
    ): ResponseEntity<Boolean> {
        val token = request.getHeader("Authorization")
        val userId = getIdByToken(token)

        val canDelete = validationService.canDelete(userId, snippetId)
        if (!canDelete) {
            logger.error("User with id $userId cannot delete snippet with id $snippetId")
        } else {
            logger.info("User with id $userId can delete snippet with id $snippetId")
        }
        return ResponseEntity.ok(canDelete)
    }
}
