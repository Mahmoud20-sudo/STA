package sa.sauditourism.employee.extensions

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import junit.framework.TestCase.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import sa.sauditourism.employee.managers.language.LanguageManager

class StringsExtensionsTest {

    @Mock
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testLocalizeString() {
        val hashMap = HashMap<String, String>()
        hashMap["VIEW_PROFILE"] = "View profile"
        LanguageManager.insertAllData("en", hashMap)
        val translation = "VIEW_PROFILE".localizeString()
        assertEquals(translation, "View profile")
    }


    @Test
    fun testEncryptToken() {
        val token = "token".encryptToken()
        assertNotEquals(token, "token")
    }
}
