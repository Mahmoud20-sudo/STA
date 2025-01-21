package sa.sauditourism.employee.modules.home.bookMeetingRoom

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.DefaultLifecycleObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import sa.sauditourism.employee.base.BaseViewModel
import sa.sauditourism.employee.extensions.toRiyadhTime
import sa.sauditourism.employee.managers.realtime.RealTimeDatabase
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject

@HiltViewModel
class BookMeetingRoomViewModel @Inject constructor(
    realTimeDatabase: RealTimeDatabase,
) : BaseViewModel(realTimeDatabase), DefaultLifecycleObserver {

    var showFromPicker = mutableStateOf(false)
    var showToPicker = mutableStateOf(false)
    var startTime = mutableStateOf("")
    var endTime = mutableStateOf("")
    var selectedDate = mutableStateOf(
        LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    )
    var isEnabled = mutableStateOf(false)
    var showError = mutableStateOf(false)
    val errorType = mutableStateOf(TimeValidationError.NONE)

    fun updateStartTime(time: String) {
        startTime.value = time
        updateIsEnabled()
    }

    fun updateEndTime(time: String) {
        endTime.value = time
        updateIsEnabled()
    }

    fun updateSelectedDate(date: String) {
        selectedDate.value = date
        updateIsEnabled()
    }

    fun hideError() {
        showError.value = false
        errorType.value = TimeValidationError.NONE
    }

    private fun updateIsEnabled() {
        isEnabled.value =
            startTime.value.isNotEmpty() && endTime.value.isNotEmpty() && selectedDate.value.isNotEmpty()
    }

    fun checkTimeValidity(): Boolean {
        if (startTime.value.isNotEmpty() && endTime.value.isNotEmpty() && selectedDate.value.isNotEmpty()) {
            try {
                val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

                val localDate = LocalDate.parse(selectedDate.value, dateFormatter)
                val startLocalTime = LocalTime.parse(startTime.value, timeFormatter)
                val endLocalTime = LocalTime.parse(endTime.value, timeFormatter)
                val startDateTime = LocalDateTime.of(localDate, startLocalTime)
                val endDateTime = LocalDateTime.of(localDate, endLocalTime)
                val duration = Duration.between(startDateTime, endDateTime)

                // Define the specific time zone you want to use, e.g., "Asia/Riyadh"
//                val zoneId = ZoneId.of("Asia/Riyadh")
                val zoneId = ZoneId.systemDefault()
                val currentDateTimeInZone = ZonedDateTime.now(zoneId)
                val currentDate = currentDateTimeInZone.toLocalDate()
                val currentDateTime = currentDateTimeInZone.toLocalDateTime()

                when {
                    //end time before current
                    localDate.isEqual(currentDate) && (endDateTime.isBefore(currentDateTime) || endDateTime.isEqual(
                        currentDateTime
                    )) -> {
                        errorType.value = TimeValidationError.END_TIME_BEFORE_OR_EQUAL_CURRENT
                        showError.value = true
                    }
                    //to == from
                    startLocalTime == endLocalTime -> {
                        errorType.value = TimeValidationError.START_TIME_EQUAL_END_TIME
                        showError.value = true
                    }
                    //to < from
                    endDateTime.isBefore(startDateTime) -> {
                        errorType.value = TimeValidationError.END_BEFORE_START
                        showError.value = true
                    }
                    //more than 2h
                    duration.toMinutes() > 120 || duration.toMinutes() <= 5 -> {
                        errorType.value = TimeValidationError.INVALID_DURATION
                        showError.value = true
                    }

                    else -> {
                        errorType.value = TimeValidationError.NONE
                        showError.value = false
                    }
                }
            } catch (e: DateTimeParseException) {
                errorType.value = TimeValidationError.INVALID_FORMAT
                showError.value = true
            }
        } else {
            errorType.value = TimeValidationError.MISSING_FIELDS
            showError.value = true
        }

        return showError.value
    }

}

enum class TimeValidationError {
    NONE,
    END_BEFORE_START,
    INVALID_DURATION,
    MISSING_FIELDS,
    INVALID_FORMAT,
    END_TIME_BEFORE_OR_EQUAL_CURRENT,
    START_TIME_EQUAL_END_TIME
}
