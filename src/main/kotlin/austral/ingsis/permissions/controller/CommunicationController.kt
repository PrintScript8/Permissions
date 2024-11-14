package austral.ingsis.permissions.controller

import austral.ingsis.permissions.server.CorrelationIdInterceptor
import com.newrelic.api.agent.Trace
import org.springframework.http.ResponseEntity
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestClient

@RestController
class CommunicationController(restClientBuilder: RestClient.Builder) {
    val myInterceptor: ClientHttpRequestInterceptor = CorrelationIdInterceptor()
    val restClient =
        restClientBuilder
            .baseUrl("http://snippet-service:8080")
            .requestInterceptor(myInterceptor).build()

    companion object {
        const val DELAY_TIME = 3000L
        const val GREETING_MESSAGE = "Greetings from Permissions!"
        const val ONLINE = "Permissions is online"
        const val DELAY = "Esta respuesta tardó 3 segundos en llegar."
    }

    @GetMapping("/testRespondMessage")
    fun respondMessage(): String {
        return GREETING_MESSAGE
    }

    @GetMapping("/testConnection")
    fun testConnection(): String {
        return ONLINE
    }

    @GetMapping("/delay")
    fun delayEndpoint(): String {
        Thread.sleep(DELAY_TIME)
        return DELAY
    }

    @GetMapping("/simulate-error")
    @Trace(dispatcher = true)
    @Suppress("TooGenericExceptionCaught", "TooGenericExceptionThrown")
    fun simulateError(): ResponseEntity<String> {
        throw RuntimeException("Error intencional para probar New Relic")
    }

    @GetMapping("/secondRepo")
    @Trace(dispatcher = true)
    fun secondRepo(): ResponseEntity<String> {
        val response =
            restClient.get()
                .uri("/hello-world")
                .retrieve()
                .toEntity(String::class.java)
        return ResponseEntity(response.body, response.statusCode)
    }

    @GetMapping("/hello-world")
    @Trace(dispatcher = true)
    fun helloWorld(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello World!")
    }
}
