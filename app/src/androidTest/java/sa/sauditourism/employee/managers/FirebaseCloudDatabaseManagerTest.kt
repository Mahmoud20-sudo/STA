package sa.sauditourism.employee.managers

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import sa.sauditourism.employee.constants.FirebaseConstants
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.managers.realtime.FirebaseDatabase

@RunWith(AndroidJUnit4::class)
class FirebaseCloudDatabaseManagerTest {

    private lateinit var firebaseDatabase: FirebaseDatabase

    @Before
    fun setUp() {
       // MockitoAnnotations.openMocks(this)
        firebaseDatabase = FirebaseDatabase()
    }

    @Test
    fun testFetchSupportedLanguages_WhenAuthenticated_ShouldFetchSupportedLanguages() {
        firebaseDatabase.fetchSupportedLanguages(MutableStateFlow(true))

        Thread.sleep(3000)

        assertTrue(firebaseDatabase.supportedLanguagesList.isNotEmpty())
    }

    @Test
    fun testFetchSupportedLanguages_WhenAuthenticated_ShouldFetchLanguageAndKeys() {
        firebaseDatabase.fetchLocalization(LanguageConstants.DEFAULT_LOCALE, MutableStateFlow(true))

        Thread.sleep(3000)

        assertTrue(firebaseDatabase.languagesAndKeys.isNotEmpty())
    }
}
