package sa.sauditourism.employee.extensions

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test
import sa.sauditourism.employee.base.BaseUnitTest

@OptIn(ExperimentalCoroutinesApi::class)
class FlagExtensionsTest: BaseUnitTest() {

    @Test
    fun `toFlagEmoji should return flag emoji for valid country code`() {
        val countryCode = "US"
        val expectedEmoji = "\uD83C\uDDFA\uD83C\uDDF8"

        val result = countryCode.toFlagEmoji()

        assertEquals(expectedEmoji, result)
    }

    @Test
    fun `toFlagEmoji should return input string for invalid country code`() {
        val invalidCountryCode = "USA"

        val result = invalidCountryCode.toFlagEmoji()

        assertEquals(invalidCountryCode, result)
    }

    @Test
    fun `toFlagEmoji should return input string for non-alphabet characters`() {
        val nonAlphabetCountryCode = "12"

        val result = nonAlphabetCountryCode.toFlagEmoji()

        assertEquals(nonAlphabetCountryCode, result)
    }

    @Test
    fun `toFlagEmoji should return input string for empty string`() {
        val emptyString = ""

        val result = emptyString.toFlagEmoji()

        assertEquals(emptyString, result)
    }
}
