package sa.sauditourism.employee.extensions

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.managers.environment.EnvironmentKeys
import java.text.NumberFormat
import java.util.Locale

class StringExtensionsTest {

    @Test
    fun testGetHashMapFromJson() {
        val jsonString = "{\"key1\":\"value1\",\"key2\":\"value2\"}"
        val hashMap = jsonString.getHashMapFromJson()

        assertEquals("value1", hashMap["key1"])
        assertEquals("value2", hashMap["key2"])
    }

    @Test
    fun testCapitalizeFirstCharOnly() {
        val originalString = "hello world"
        val capitalizedString = originalString.capitalizeFirstCharOnly()

        assertEquals("Hello world", capitalizedString)
    }

    @Test
    fun testIsValidEmail() {
        assertTrue("test@example.com".isValidEmail())
        assertFalse("test@example".isValidEmail())
        assertFalse("testexample.com".isValidEmail())
    }

    @Test
    fun testIsValidPassword() {
        assertTrue("Password@1".isValidPassword())
        assertFalse("password".isValidPassword())
        assertFalse("password123".isValidPassword())
    }

    @Test
    fun testConvertArabic() {
        val arabicNumber = "١٢٣٤٥٦٧٨٩٠"
        val expected = "1234567890"

        assertEquals(expected, arabicNumber.convertArabic())
    }

    @Test
    fun testNumberCurrency() {
        val numberString = "123456789"
        val formattedNumber = numberString.numberCurrency()

        assertEquals("123,456,789", formattedNumber)
    }
}
fun String.capitalizeFirstCharOnly(): String {
    return this.lowercase().replaceFirstChar {
        if (it.isLowerCase()) {
            it.titlecase(
                Locale.getDefault(),
            )
        } else {
            it.toString()
        }
    }
}

fun String.isValidEmail(): Boolean {
    val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
    return matches(emailRegex)
}

fun String.isValidPassword(): Boolean {
    val passwordRegex = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-zA-Z])(?=.*[@#$%^&+_=]).{8,}$".toRegex()
    return matches(passwordRegex)
}

fun String.toWebsiteUrl(): String {
    return if (this.startsWith("http")) {
        this
    } else {
        "${EmployeeApplication.instance.environmentManager.getVariable(EnvironmentKeys.API_BASE_URL)}$this"
    }
}
fun String.numberCurrency(): String {
    return try {
        NumberFormat.getInstance(Locale.ENGLISH).format(this.toInt())
    } catch (ex: Exception) {
        this
    }
}