package sa.sauditourism.employee.extensions

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test
import sa.sauditourism.employee.base.BaseUnitTest
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class DateFormatExtensionsTest: BaseUnitTest() {

    @Test
    fun testDateToFormattedString() {
        val date = Date(1626766496000L) // July 20, 2021, 10:34:56 PM UTC
        val formattedDate = date.toFormattedString("yyyy-MM-dd HH:mm:ss")
        assertEquals("2021-07-20 10:34:56", formattedDate)
    }

    @Test
    fun testStringToDate() {
        val dateString = "2023-07-20 12:34:56"
        val date = dateString.toDate("yyyy-MM-dd HH:mm:ss")
        val expectedDate = Date(1689845696000L) // July 20, 2023, 12:34:56 PM UTC
        assertEquals(expectedDate, date)
    }

    @Test
    fun testLongToFormattedString() {
        val timestamp = 1626766496000L // July 20, 2021, 10:34:56 PM UTC
        val formattedDate = timestamp.toFormattedString("yyyy-MM-dd HH:mm:ss")
        assertEquals("2021-07-20 10:34:56", formattedDate)
    }

    @Test
    fun testStringToFormattedString() {
        val dateString = "2023-07-20 10:34:56"
        val formattedDate = dateString.toFormattedString(
            "yyyy-MM-dd HH:mm:ss",
            "MMMM dd, yyyy",
            Locale.US
        )
        assertEquals("July 20, 2023", formattedDate)
    }
}
