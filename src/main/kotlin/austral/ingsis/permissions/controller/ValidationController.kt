package austral.ingsis.permissions.controller

import austral.ingsis.permissions.service.ValidationService
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/validate")
class ValidationController(
    @Autowired private val validationService: ValidationService,
) {
    private val logger: Logger = LogManager.getLogger(ValidationController::class.java)

    @PutMapping("/create/{id}")
    fun createSnippet(
        @PathVariable id: Long,
    ): ResponseEntity<Boolean> {
        val exists = validationService.exists(id)
        if (!exists) {
            logger.error("User with id $id does not exist")
        } else {
            logger.info("User with id $id exists")
        }
        return ResponseEntity.ok(exists)
    }

    @PutMapping("/edit/{id}/{snippetId}")
    fun editSnippet(
        @PathVariable id: Long,
        @PathVariable snippetId: Long,
    ): ResponseEntity<Boolean> {
        val canModify = validationService.canModify(id, snippetId)
        if (!canModify) {
            logger.error("User with id $id cannot modify snippet with id $snippetId")
        } else {
            logger.info("User with id $id can modify snippet with id $snippetId")
        }
        return ResponseEntity.ok(canModify)
    }

    @PutMapping("/read/{id}/{snippetId}")
    fun readSnippet(
        @PathVariable id: Long,
        @PathVariable snippetId: Long,
    ): ResponseEntity<Boolean> {
        val canRead = validationService.canRead(id, snippetId)
        if (!canRead) {
            logger.error("User with id $id cannot read snippet with id $snippetId")
        } else {
            logger.info("User with id $id can read snippet with id $snippetId")
        }
        return ResponseEntity.ok(canRead)
    }

    @PutMapping("/delete/{id}/{snippetId}")
    fun deleteSnippet(
        @PathVariable id: Long,
        @PathVariable snippetId: Long,
    ): ResponseEntity<Boolean> {
        val canDelete = validationService.canDelete(id, snippetId)
        if (!canDelete) {
            logger.error("User with id $id cannot delete snippet with id $snippetId")
        } else {
            logger.info("User with id $id can delete snippet with id $snippetId")
        }
        return ResponseEntity.ok(canDelete)
    }
}
