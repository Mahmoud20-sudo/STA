package sa.sauditourism.employee.extensions

import sa.sauditourism.employee.constants.DateConstants
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Date.toFormattedString(
    format: String,
    locale: Locale = Locale.getDefault(),
    timeZone: TimeZone = TimeZone.getTimeZone("Asia/Riyadh")
): String {
    val formatter = SimpleDateFormat(format, locale)
    formatter.timeZone = timeZone
    return formatter.format(this).convertArabic()
}

fun Long.toFormattedDate(format: String, locale: Locale = Locale.getDefault()): String {
    val date = Date(this)
    return date.toFormattedString(format, locale).convertArabic()
}

fun Date.getCurrentDateToAnalytics(): String {
    val formatter = SimpleDateFormat(DateConstants.DD_MM_YYYY_HH_MM_SS_CAMEL, Locale.ENGLISH)
    formatter.timeZone = TimeZone.getTimeZone("Asia/Riyadh")
    return formatter.format(this).convertArabic()
}

fun Long.toFormattedString(format: String, locale: Locale = Locale.getDefault()): String {
    val date = Date(this)
    return date.toFormattedString(format, locale).convertArabic()
}

fun String.toFormattedString(
    fromFormat: String,
    toFormat: String,
    locale: Locale = Locale.getDefault()
): String {
    val date = this.toDate(fromFormat, locale, "UTC")
    return date?.toFormattedString(toFormat, locale)?.convertArabic() ?: ""
}


fun String.toDate(
    format: String,
    locale: Locale = Locale.getDefault(),
    timeZone: String = "Asia/Riyadh"
): Date? {
    val formatter = SimpleDateFormat(format, locale)
    formatter.timeZone = TimeZone.getTimeZone(timeZone)
    return try {
        formatter.parse(this)
    } catch (e: Exception) {
        null
    }
}

fun String.formatDateFromTo(fromFormat: String, toFormat: String): String {
    return if (!isNullOrBlank()) {
        val inputFormatter = DateTimeFormatter.ofPattern(fromFormat, Locale.getDefault())
        val outputFormatter = DateTimeFormatter.ofPattern(toFormat, Locale.getDefault())

        val date = LocalDate.parse(this, inputFormatter)
        date.format(outputFormatter)
    } else ""
}

fun String.formatTimeFromTo(fromFormat: String, toFormat: String): String {
    return if (!isNullOrBlank()) {
        val inputFormatter = DateTimeFormatter.ofPattern(fromFormat, Locale.getDefault())
        val outputFormatter = DateTimeFormatter.ofPattern(toFormat, Locale.getDefault())

        val date = LocalTime.parse(this, inputFormatter)
        date.format(outputFormatter)
    } else ""
}

fun Date.getDayMonthYear(locale: Locale = Locale.getDefault()): Map<String, String>? {
    try {
        val timeZone = TimeZone.getTimeZone("Asia/Riyadh")
        val retVal = HashMap<String, String>()
        val calendar = Calendar.getInstance(timeZone, locale)
        calendar.time = this

        retVal[DateConstants.DAY_KEY] =
            "%02d".format(calendar.get(Calendar.DAY_OF_MONTH)).convertArabic()
        retVal[DateConstants.MONTH_KEY] =
            SimpleDateFormat(DateConstants.MMM, locale).format(calendar.time).convertArabic()
        retVal[DateConstants.YEAR_KEY] =
            SimpleDateFormat("YYYY", locale).format(calendar.time).convertArabic()
        return retVal

    } catch (ignored: Exception) {
        return null
    }
}

// convert any time string with its format to Sa time

fun String.toRiyadhTime(format: String): String {
    val inputFormatter = DateTimeFormatter.ofPattern(format)
    val inputTime = LocalTime.parse(this, inputFormatter)

    val currentZoneId = ZoneId.systemDefault()
    val riyadhZoneId = ZoneId.of("Asia/Riyadh")

    val adjustedTime = if (currentZoneId != riyadhZoneId) {
        // Convert the time to Riyadh time
        ZonedDateTime.now(currentZoneId)
            .with(inputTime)
            .withZoneSameInstant(riyadhZoneId)
            .toLocalTime()
    } else {
        inputTime
    }

    // Use HH:mm for 24-hour format
    val outputFormatter = DateTimeFormatter.ofPattern("HH:mm")
    return adjustedTime.format(outputFormatter)
}

