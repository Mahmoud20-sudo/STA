package sa.sauditourism.employee.extensions

import org.junit.Assert.assertEquals
import org.junit.Test
import sa.sauditourism.employee.constants.DateConstants
import java.util.*

class DateExtensionsTest {

    @Test
    fun testDateToFormattedString() {
        val date = Calendar.getInstance().apply {
            set(2024, Calendar.FEBRUARY, 27, 14, 30, 0)
        }.time

        val formattedDate = date.toFormattedString("dd-MM-yyyy HH:mm:ss")
        assertEquals("27-02-2024 14:30:00", formattedDate)
    }

    @Test
    fun testGetCurrentDateToAnalytics() {
        val date = Calendar.getInstance().apply {
            set(2024, Calendar.FEBRUARY, 27, 14, 30, 0)
        }.time

        val formattedDate = date.getCurrentDateToAnalytics()
            .toFormattedString("ddMMyyyyHHmmss", "dd-MM-yyyy HH:mm:ss")
        assertEquals("27-02-2024 14:30:00", formattedDate)
    }

    @Test
    fun testGetDayMonthYear() {
        val date = Calendar.getInstance().apply {
            set(2024, Calendar.FEBRUARY, 27, 14, 30, 0)
        }.time

        val dayMonthYear = date.getDayMonthYear(Locale.ENGLISH)

        assertEquals("27", dayMonthYear?.get(DateConstants.DAY_KEY) ?: "")
        assertEquals("Feb", dayMonthYear?.get(DateConstants.MONTH_KEY) ?: "")
        assertEquals("2024", dayMonthYear?.get(DateConstants.YEAR_KEY) ?: "")
    }

    @Test
    fun testToDate() {
        val dateString = "27-02-2024 14:30:00"
        val date = dateString.toDate("dd-MM-yyyy HH:mm:ss")

        val calendar = Calendar.getInstance().apply {
            set(2024, Calendar.FEBRUARY, 27, 14, 30, 0)
        }.time

        assertEquals(calendar.toString(), date.toString())
    }

    @Test
    fun testLongToFormattedString() {
        val milliseconds = 1645966200000 // 27-02-2022 14:50:00
        val formattedDate = milliseconds.toFormattedString("dd-MM-yyyy HH:mm:ss")

        assertEquals("27-02-2022 14:50:00", formattedDate)
    }

    @Test
    fun testStringToFormattedString() {
        val dateString = "27-02-2024 14:30:00"
        val formattedDate = dateString.toFormattedString("dd-MM-yyyy HH:mm:ss", "yyyy-MM-dd")

        assertEquals("2024-02-27", formattedDate)
    }
}
