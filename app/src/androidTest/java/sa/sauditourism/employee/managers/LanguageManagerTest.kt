package sa.sauditourism.employee.managers

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import sa.sauditourism.employee.managers.language.LanguageManager
import sa.sauditourism.employee.managers.language.model.SupportedLanguage
import sa.sauditourism.employee.managers.sharedprefrences.SharedPreferencesManager

@RunWith(AndroidJUnit4::class)
class LanguageManagerTest {

    private lateinit var languageManager: LanguageManager

    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    var supportedLanguages = ArrayList<SupportedLanguage>()


    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        sharedPreferencesManager = SharedPreferencesManager(context)
        languageManager = LanguageManager
        runBlocking(Dispatchers.Main) {
            languageManager.localizationChanged.value = null
        }
    }

    @Test
    fun testGetTranslationByKey() {
        // Verify that a valid translation key returns the corresponding translation value
        val key = "app_name"
        val expectedValue = "Visit Saudi"
        languageManager.insertAllData(
            "en",
            hashMapOf(
                key to expectedValue
            )
        )
        val actualValue = languageManager.getTranslationByKey(key)
        assertEquals(expectedValue, actualValue)

        // Verify that an invalid translation key returns the key itself
        val invalidKey = "invalid_key"
        val actualFallbackValue = languageManager.getTranslationByKey(invalidKey)
        assertEquals(invalidKey, actualFallbackValue)
    }

    @Test
    fun testInsertAllData() {
        // Verify that inserting localized data updates the localization data
        val lang = "en"
        val data = hashMapOf(
            "app_name" to "Visit Saudi",
            "welcome_message" to "Welcome to Saudi Arabia"
        )
        languageManager.insertAllData(lang, data)
        assertEquals(data, languageManager.getLocalizationData())
    }

    @Test
    fun testSetApplicationLanguage() {
        // Verify that setting the application language updates the preferred locale
        val lang = "en_US"
        languageManager.setApplicationLanguage(lang)

        assertEquals(lang, sharedPreferencesManager.preferredLocale)
    }

    @Test
    fun testChangeLanguage() {
        // Verify that changing the language updates the preferred locale and triggers localizationChanged
        val lang = "ar"
        runBlocking(Dispatchers.Main) {
            languageManager.changeLanguage(lang)
        }
        assertEquals(lang, sharedPreferencesManager.preferredLocale)
        assertTrue(languageManager.localizationChanged.value!!)
    }
}
