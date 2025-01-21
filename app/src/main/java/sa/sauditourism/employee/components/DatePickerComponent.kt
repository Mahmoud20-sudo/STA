package sa.sauditourism.employee.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import sa.sauditourism.employee.extensions.clickableWithoutRipple
import sa.sauditourism.employee.extensions.conditional
import sa.sauditourism.employee.extensions.formatDateFromTo
import sa.sauditourism.employee.extensions.toFormattedDate
import sa.sauditourism.employee.resources.AppColors
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerComponent(
    informativeMessage: String? = null,
    label: String? = null,
    isOptional: Boolean = false,
    readOnly: Boolean = false,
    // to allow user to show the date picker always
    showOnlyPicker: Boolean = false,
    // restrict user form choosing the previous days
    restrictPastDates: Boolean = false,
    defaultValue: String = "",
    state: DatePickerState,
    modifier: Modifier = Modifier.padding(20.dp),
    datePickerShadow: Dp = 2.dp,
    dateFormatter: DatePickerFormatter = remember { DatePickerDefaults.dateFormatter() },
    title: @Composable() (() -> Unit)? = {
    },
    headline: @Composable() (() -> Unit)? = {
        DatePickerDefaults.DatePickerHeadline(
            selectedDateMillis = state.selectedDateMillis,
            displayMode = state.displayMode,
            dateFormatter = dateFormatter,
            modifier = Modifier.padding(all = 10.dp)
        )
    },
    showModeToggle: Boolean = false,
    showPicker: Boolean = showOnlyPicker,
    onPickerStateChange: (Boolean) -> Unit = {},
    datePickerHeight: Dp? = null,
    paddingStart: Dp? = null,
    containerHeight: Dp = 44.dp,
    dateViewFormat: String? = "yyyy-MM-dd",
    filledBorderColor: Color? = AppColors.OnPrimaryDark,
    onDateSelected: (String) -> Unit = {}

) {

    val selectableDates = if (restrictPastDates) {
        // Restrict past dates
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                // Convert to local date
                val currentDate = LocalDate.now()
                val selectedDate =
                    Instant.ofEpochMilli(utcTimeMillis).atZone(ZoneId.systemDefault()).toLocalDate()

                // Allow selecting today or future dates
                return !selectedDate.isBefore(currentDate)
            }

            override fun isSelectableYear(year: Int): Boolean {
                // Allow current or future years
                return year >= LocalDate.now().year
            }
        }
    } else {
        // Allow all dates (no restrictions)
        DatePickerDefaults.AllDates
    }

    val datePickerState = rememberDatePickerState(
        selectableDates = selectableDates,
        initialSelectedDateMillis = state.selectedDateMillis
    )
//    var showDatePicker by remember { mutableStateOf(false) }
    var datePickerValue = remember { mutableStateOf(defaultValue) }

    // Update datePickerValue when the date is selected
    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let {
            datePickerValue.value = it.toFormattedDate("yyyy-MM-dd")
            onPickerStateChange.invoke(false)
            onDateSelected.invoke(datePickerValue.value)
        }
    }

    key(datePickerValue.value) {
        Column(
            modifier
                .fillMaxWidth()
                .clickableWithoutRipple {
                    onPickerStateChange.invoke(showPicker.not())
                }
        ) {
            if (!showOnlyPicker)
                InputFieldComponent(
                    paddingStart = paddingStart,
                    title = label,
                    clickable = true,
                    enabled = !readOnly,
                    readOnly = readOnly,
                    isOptional = isOptional,
                    informativeMessage = informativeMessage,
                    value = datePickerValue.value.formatDateFromTo(
                        "yyyy-MM-dd",
                        dateViewFormat ?: "yyyy-MM-dd"
                    ),
                    onValueChange = { /* No action needed */ },
                    inputFieldModel = InputFieldModel(
                        type = InputFieldType.DatePicker,
                        placeholderText = ""
                    ),
                    modifier = Modifier.clickableWithoutRipple {
                        onPickerStateChange.invoke(showPicker.not())
                    },
                    minTextFieldHeight = containerHeight,
                    filledBorderColor = filledBorderColor,

                    )
            if (showPicker) {
                BoxWithConstraints {
                    // 360 is minimum because DatePicker uses 12.dp horizontal padding and 48.dp for each week day
                    val scale =
                        remember(this.maxWidth) { if (this.maxWidth > 360.dp) 1f else (this.maxWidth / 360.dp) }
                    // Make sure there is always enough room, so use requiredWidthIn
                    Box(
                        modifier = Modifier
                            .requiredWidthIn(min = 360.dp)
                            .height(370.dp)
                    ) {
                        // Scale in case the width is too large for the screen
                        DatePicker(
                            state = datePickerState,
                            modifier = Modifier
                                .scale(scale)
                                // added radius as 12.dp
                                .background(
                                    shape =
                                    RoundedCornerShape(12.dp),
                                    color = Color.White
                                )
                                .shadow(
                                    datePickerShadow,
                                    RoundedCornerShape(12.dp),

                                    )
                                .zIndex(8f)
                                .conditional(datePickerHeight != null) {
                                    height(datePickerHeight!!)
                                },
                            dateFormatter = dateFormatter,
                            title = null,
                            headline = null,
                            showModeToggle = showModeToggle,

                            colors = DatePickerDefaults.colors(
                                selectedDayContainerColor = AppColors.OnSecondaryLight,
                                selectedDayContentColor = Color.White,
                                todayDateBorderColor = AppColors.OnSecondaryLight,
                                containerColor = Color.White
                            ),

                            )
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreviewDatePickerComponent() {
    val state = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    DatePickerComponent(state = state)
}