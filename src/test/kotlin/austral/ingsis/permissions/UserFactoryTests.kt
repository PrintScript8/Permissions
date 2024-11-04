package austral.ingsis.permissions

import austral.ingsis.permissions.factory.UserFactory
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class UserFactoryTests {
    private val userFactory: UserFactory = UserFactory()

    @Test
    fun createUserTest() {
        val user = userFactory.buildUser(listOf(), listOf())

        Assertions.assertEquals(0L, user.id)
        Assertions.assertEquals(listOf<Long>(), user.owner)
        Assertions.assertEquals(listOf<Long>(), user.collaborator)
    }
}
