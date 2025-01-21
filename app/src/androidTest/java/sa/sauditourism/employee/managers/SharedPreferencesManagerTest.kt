package sa.sauditourism.employee.managers

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import sa.sauditourism.employee.managers.sharedprefrences.SharedPreferencesManager

@RunWith(AndroidJUnit4::class)
class SharedPreferencesManagerTest {

    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        sharedPreferencesManager = SharedPreferencesManager(context)
        sharedPreferencesManager.clearAllPreferences()
    }

    @Test
    fun testDarkModePreference() {
        // Verify that the default value for dark mode is false
        assertFalse(sharedPreferencesManager.darkMode)

        // Set the value of dark mode to true
        sharedPreferencesManager.darkMode = true
        assertTrue(sharedPreferencesManager.darkMode)

        // Set the value of dark mode back to false
        sharedPreferencesManager.darkMode = false
        assertFalse(sharedPreferencesManager.darkMode)
    }

    @Test
    fun testEmailPreference() {
        // Verify that the default value for email is null
        assertNull(sharedPreferencesManager.email)

        // Set the value of email to a non-null value
        val email = "test@example.com"
        sharedPreferencesManager.email = email
        assertEquals(email, sharedPreferencesManager.email)

        // Set the value of email back to null
        sharedPreferencesManager.email = null
        assertNull(sharedPreferencesManager.email)
    }

    @Test
    fun testPreferredLocalePreference() {
        // Verify that the default value for preferred locale is null
        assertNull(sharedPreferencesManager.preferredLocale)

        // Set the value of preferred locale to a non-null value
        val locale = "en"
        sharedPreferencesManager.preferredLocale = locale
        assertEquals(locale, sharedPreferencesManager.preferredLocale)

        // Set the value of preferred locale back to null
        sharedPreferencesManager.preferredLocale = null
        assertNull(sharedPreferencesManager.preferredLocale)
    }

}
