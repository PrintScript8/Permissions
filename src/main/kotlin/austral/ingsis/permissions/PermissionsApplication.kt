package austral.ingsis.permissions

import io.github.cdimascio.dotenv.Dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PermissionsApplication

fun main(args: Array<String>) {
    Dotenv.load()
    runApplication<PermissionsApplication>(args.toString())
}
