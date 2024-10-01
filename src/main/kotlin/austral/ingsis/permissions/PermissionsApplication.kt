package austral.ingsis.permissions

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import io.github.cdimascio.dotenv.Dotenv

@SpringBootApplication
class PermissionsApplication

fun main(args: Array<String>) {
	val dotenv = Dotenv.load()
	dotenv.entries().forEach { entry -> System.setProperty(entry.key, entry.value) }
	runApplication<PermissionsApplication>(*args)
}

