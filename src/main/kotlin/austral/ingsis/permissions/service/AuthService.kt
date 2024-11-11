package austral.ingsis.permissions.service

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate
import java.net.UnknownHostException

@Service
class AuthService {
        private val restTemplate = RestTemplate()

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
                if (response.statusCode == HttpStatus.UNAUTHORIZED) {
                    return null
                }

                // Extract 'id' from the response body if status is OK
                if (response.statusCode == HttpStatus.OK) {
                    val responseBody = response.body
                    return responseBody?.get("id") as? String
                }

                null
            } catch (e: UnknownHostException) {
                null
            } catch (e: ResourceAccessException) {
                null
            }
        }
}