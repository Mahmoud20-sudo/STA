package sa.sauditourism.employee.extensions

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock

class ContextExtensionsTest {

    @Mock
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testIsNetworkAvailable() {
        val isNetworkAvailable = context.isNetworkAvailable()
        Assert.assertNotNull(isNetworkAvailable)
    }

    @Test
    fun testIsGooglePlayServiceAvailable() {
        val isGooglePlayServiceAvailable = context.isGooglePlayServiceAvailable()
        Assert.assertNotNull(isGooglePlayServiceAvailable)
    }

    @Test
    fun testOpenGooglePlayStoreForApp() {
        // Check if Google play store app is opened on device for our app
        context.openStoreForApp("sa.sauditourism.employee")
    }

    @Test
    fun testGetStringResourceByName() {
        val stringResource = context.getStringResourceByName("app_name")
        Assert.assertNotNull(stringResource)
        Assert.assertNotEquals(stringResource, "")
    }

    @Test
    fun testOpenWhatsapp() {
        // Check if whatsapp is opened on device for this number
        // make sure country code is correct (i.e. +962)
        context.openWhatsapp("+9627992299229922")
    }

    @Test
    fun testOpenMail() {
        // Check if Mail app is opened on device for this email
        context.openMail("test@test.com")
    }

    @Test
    fun testOpenPhone() {
        // Check if dialpad is opened on device for this number
        // make sure country code is correct (i.e. +962)
        context.openPhone("+9627992299229922")
    }

    @Test
    fun testOpenExternalBrowser() {
        // Check if Browser app is opened on device for this website
        context.openExternalBrowser("https://twitter.com")
    }

}
