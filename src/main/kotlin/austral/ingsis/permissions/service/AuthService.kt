package austral.ingsis.permissions.service

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate
import java.net.UnknownHostException

@Service
class AuthService {
    private val restTemplate = RestTemplate()

    @Suppress("TooGenericExceptionCaught", "SwallowedException")
    fun validateToken(token: String): String? {
        return try {
            // Prepare headers with the Authorization token
            val headers = HttpHeaders()
            headers.set("Authorization", token)
            val requestEntity = HttpEntity<String>(headers)

            // Send request to authorization service
            val response =
                restTemplate.exchange(
                    "http://authorization:8087/authorize/auth0",
                    HttpMethod.POST,
                    requestEntity,
                    Map::class.java,
                )

            // Check response status
            return when (response.statusCode) {
                HttpStatus.UNAUTHORIZED -> null
                HttpStatus.OK -> {
                    val responseBody = response.body
                    responseBody?.get("id") as? String
                }
                else -> null
            }

        } catch (e: Exception){
            null
        }
    }
}
