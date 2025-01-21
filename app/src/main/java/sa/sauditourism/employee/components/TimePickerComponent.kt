package sa.sauditourism.employee.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.clickableWithoutRipple
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.extensions.toFormattedString
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.resources.AppColors.OnPrimaryDark
import sa.sauditourism.employee.resources.AppFonts.copy375
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerComponent(
    informativeMessage: String? = null,
    label: String? = null,
    isOptional: Boolean = false,
    showPicker: Boolean = false,
    readOnly: Boolean = false,
    defaultValue: String = "",
    modifier: Modifier = Modifier.padding(20.dp),
    onDismissRequest: () -> Unit,
    onPickerStateChange: (Boolean) -> Unit = {},
    onConfirmRequest: (String) -> Unit,
    placeHolderText: String = "", //to c
    containerHeight: Dp = 44.dp,
    filledBorderColor: Color? = AppColors.OnPrimaryDark,
    textColor: Color? = AppColors.ColorsPrimitivesTwo,
    outputFormat: String? = "HH:mm:ss.sssZ",
    error: String? = "",
    showErrorMessage: Boolean? = true,
    showDefaultTimeOnDialog: Boolean? = false,
    useSaudiFormatter: Boolean? = true
) {

    //    var showDatePicker by remember { mutableStateOf(false) }
    var datePickerValue = remember { mutableStateOf(defaultValue) }
    SideEffect {
        datePickerValue.value = defaultValue
    }


    key(datePickerValue.value) {
        Column(
            modifier
                .fillMaxWidth()
                .clickableWithoutRipple {
                    onPickerStateChange.invoke(showPicker.not())
                }) {
            InputFieldComponent(
                title = label,
                minTextFieldHeight = containerHeight,
                clickable = true,
                enabled = !readOnly,
                readOnly = readOnly,
                isOptional = isOptional,
                informativeMessage = informativeMessage,
                value = datePickerValue.value,
                onValueChange = { /* No action needed */ },
                inputFieldModel = InputFieldModel(
                    type = InputFieldType.Time,
                    placeholderText = placeHolderText,
                    textColor = textColor!!
                ),
                modifier = Modifier.clickableWithoutRipple {
                    onPickerStateChange.invoke(showPicker.not())
                },
                textStyle = copy375,
                paddingStart = 12.dp,
                filledBorderColor = filledBorderColor,
                errorMessage = error,
                showErrorMessage = showErrorMessage
            )
        }
        if (showPicker) {
            Dialog(
                onDismissRequest = { onPickerStateChange.invoke(false) },
                properties = DialogProperties(
                    usePlatformDefaultWidth = false
                ),
            ) {
                val timePickerState =
                    if (showDefaultTimeOnDialog == true) {
                        if (defaultValue.isNotEmpty()) {
                            val defaultTime = LocalTime.parse(
                                defaultValue,
                                DateTimeFormatter.ofPattern("hh:mm a")
                            )
                            rememberTimePickerState(
                                initialHour = defaultTime.hour,
                                initialMinute = defaultTime.minute,
                            )
                        } else {
                            // Get the current time in Saudi Arabia
                            val saudiArabiaTime =
                                ZonedDateTime.now(
                                    if (useSaudiFormatter == true)
                                        ZoneId.of("Asia/Riyadh")
                                    else
                                        ZoneId.systemDefault()
                                ).toLocalTime()
                            rememberTimePickerState(
                                initialHour = saudiArabiaTime.hour,
                                initialMinute = saudiArabiaTime.minute
                            )
                        }


                    } else {
                        rememberTimePickerState()
                    }
                Surface(
                    shape = MaterialTheme.shapes.extraLarge,
                    tonalElevation = 6.dp,
                    modifier = Modifier.run {
                        width(IntrinsicSize.Min)
                            .height(IntrinsicSize.Min)
                            .background(
                                shape = MaterialTheme.shapes.extraLarge, color = Color.White
                            )
                    },
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        TimePicker(
                            state = timePickerState,
                            layoutType = TimePickerLayoutType.Vertical,
                            colors = TimePickerColors(
                                clockDialColor = Color.White,
                                selectorColor = OnPrimaryDark,
                                containerColor = Color.White,
                                periodSelectorBorderColor = OnPrimaryDark,
                                clockDialSelectedContentColor = Color.White,
                                clockDialUnselectedContentColor = OnPrimaryDark,
                                periodSelectorSelectedContainerColor = OnPrimaryDark,
                                periodSelectorUnselectedContainerColor = Color.White,
                                periodSelectorSelectedContentColor = Color.White,
                                periodSelectorUnselectedContentColor = OnPrimaryDark,
                                timeSelectorSelectedContainerColor = Color.White,
                                timeSelectorUnselectedContainerColor = Color.White,
                                timeSelectorSelectedContentColor = OnPrimaryDark,
                                timeSelectorUnselectedContentColor = OnPrimaryDark
                            )
                        )

                        Row(
                            modifier = Modifier
                                .height(40.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            ButtonComponent(
                                text = LanguageConstants.CANCEL.localizeString(),
                                clickListener = { onPickerStateChange.invoke(false) },
                                buttonType = ButtonType.Dissmiss
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            ButtonComponent(text = LanguageConstants.DONE_BUTTON_TITLE.localizeString(),
                                clickListener = {
                                    onPickerStateChange.invoke(false)
                                    val calendar = Calendar.getInstance()
                                    val hour = if (timePickerState.isAfternoon) {
                                        if (timePickerState.hour < 12) timePickerState.hour + 12 else timePickerState.hour
                                    } else {
                                        if (timePickerState.hour == 12) 0 else timePickerState.hour
                                    }
                                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                                    calendar.set(Calendar.MINUTE, timePickerState.minute)
                                    datePickerValue.value =
                                        calendar.time.toFormattedString(
                                            outputFormat ?: "HH:mm:ss.sssZ",
                                            timeZone = if (useSaudiFormatter == true) TimeZone.getTimeZone(
                                                "Asia/Riyadh"
                                            ) else TimeZone.getDefault()
                                        )//10:55:26.333+0300
                                    onConfirmRequest.invoke(datePickerValue.value)
                                })
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewTimePickerComponent() {
    TimePickerComponent(onDismissRequest = {}, onConfirmRequest = { time ->
    })
}