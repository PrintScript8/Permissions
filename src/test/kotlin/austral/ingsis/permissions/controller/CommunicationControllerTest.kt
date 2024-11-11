package austral.ingsis.permissions.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(CommunicationController::class)
class CommunicationControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `respondMessage should return greetings`() {
        mockMvc.perform(get("/testRespondMessage"))
            .andExpect(status().isOk)
            .andExpect(content().string("Greetings from Permissions!"))
    }

    @Test
    fun `testConnection should return online status`() {
        mockMvc.perform(get("/testConnection"))
            .andExpect(status().isOk)
            .andExpect(content().string("Permissions is online"))
    }
}
