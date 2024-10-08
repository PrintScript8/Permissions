package austral.ingsis.permissions

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PermissionsApplication

fun main(args: Array<String>) {
    val dotenv = Dotenv.load()
    dotenv.entries().forEach { entry -> System.setProperty(entry.key, entry.value) }
    runApplication<PermissionsApplication>(args.toString())
}
