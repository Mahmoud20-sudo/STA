package sa.sauditourism.employee.managers

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import sa.sauditourism.employee.managers.realtime.FirebaseAuthenticationManager

@RunWith(AndroidJUnit4::class)
class FirebaseAuthenticationManagerTest {

    private lateinit var authManager: FirebaseAuthenticationManager

    @Before
    fun setUp() {
        authManager = FirebaseAuthenticationManager
    }

    @Test
    fun testAuthenticateDatabase_ShouldReturnTrue() {
        var result = false
        authManager.authenticateDatabase { isAuthenticated ->
            result = isAuthenticated
        }

        assert(result)
    }
}
