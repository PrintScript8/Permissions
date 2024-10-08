package austral.ingsis.permissions

import austral.ingsis.permissions.factory.UserFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class UserFactoryTests {
    private val userFactory: UserFactory = UserFactory()

    @Test
    fun createUserTest() {
        val user = userFactory.buildUser("john", "noOne@mail.com", "1234")

        Assertions.assertEquals("john", user.name)
        Assertions.assertEquals("noOne@mail.com", user.email)
        Assertions.assertEquals("1234", user.password)
    }
}
