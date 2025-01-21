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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.clickableWithoutRipple
import sa.sauditourism.employee.extensions.isNotNull
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.extensions.toFormattedDate
import sa.sauditourism.employee.extensions.toFormattedString
import sa.sauditourism.employee.modules.PastOrPresentSelectableDates
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.resources.AppColors.OnPrimaryDark
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerComponent(
    informativeMessage: String? = null,
    label: String? = null,
    isOptional: Boolean = false,
    showPicker: Boolean = false,
    readOnly: Boolean = false,
    defaultValue: String = "",
    modifier: Modifier = Modifier.padding(50.dp),
    onPickerStateChange: (Boolean) -> Unit = {},
    onDoneClick: (String) -> Unit = {}
) {
    var datePickerValue = remember { mutableStateOf(defaultValue) }
    var formattedDatePickerValue = remember { mutableStateOf(defaultValue) }

    val selectedTime = remember {
        mutableStateOf(System.currentTimeMillis().toFormattedDate("hh:mm:ss.sssZ"))
    }

    val formattedSelectedTime = remember {
        mutableStateOf(System.currentTimeMillis().toFormattedDate("hh:mm a"))
    }
    val datePickerState = rememberDatePickerState(selectableDates = PastOrPresentSelectableDates)
    val showTimePicker = remember { mutableStateOf(false) }

    key(datePickerValue.value) {
        Column(
            modifier
                .fillMaxWidth()
        ) {
            InputFieldComponent(
                title = label,
                clickable = true,
                enabled = !readOnly,
                readOnly = readOnly,
                isOptional = isOptional,
                informativeMessage = informativeMessage,
                value = formattedDatePickerValue.value,
                onValueChange = { /* No action needed */ },
                inputFieldModel = InputFieldModel(
                    type = InputFieldType.DatePicker,
                    placeholderText = ""
                ),
                modifier = Modifier.clickableWithoutRipple {
                    onPickerStateChange.invoke(showPicker.not())
                }
            )
        }

        if (showPicker) {
            Column(
                modifier = modifier
                    .padding(top = 2.dp)
                    .shadow(2.dp, RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp))
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .fillMaxWidth()
                    .zIndex(8f)
            ) {
                //DatePicker Component
                DatePicker(
                    state = datePickerState,
                    modifier = Modifier.fillMaxWidth(),
                    colors = DatePickerDefaults.colors(
                        selectedDayContainerColor = AppColors.OnSecondaryLight,
                        selectedDayContentColor = Color.White,
                        todayDateBorderColor = AppColors.OnSecondaryLight,
                        containerColor = Color.White
                    ),
                    title = null,
                    headline = null
                )
                //TimePicker Component
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = LanguageConstants.TIME.localizeString(),
                        modifier = Modifier.padding(vertical = 20.dp),
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Text(text = formattedSelectedTime.value,
                        modifier = Modifier
                            .clickableWithoutRipple(enabled = datePickerState.selectedDateMillis.isNotNull()) {
                                //open TimePicker
                                showTimePicker.value = true
                            }
                            .background(
                                Color.Gray,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(10.dp),
                        color = Color.White)

                }

                ButtonComponent(
                    text = LanguageConstants.DONE_BUTTON_TITLE.localizeString(),
                    modifier = Modifier.padding(start = 24.dp, bottom = 16.dp), clickListener = {
                        onPickerStateChange.invoke(false)
                        datePickerValue.value = if (datePickerState.selectedDateMillis.isNotNull())
                            "${datePickerState.selectedDateMillis?.toFormattedDate("yyyy-MM-dd")}T${selectedTime.value}"
                        else ""

                        formattedDatePickerValue.value = if (datePickerState.selectedDateMillis.isNotNull()) "${datePickerState.selectedDateMillis?.toFormattedDate("yyyy-MM-dd")} ${formattedSelectedTime.value}" else ""
                        onDoneClick(datePickerValue.value)
                    })
            }
        }
        if (showTimePicker.value) {
            Dialog(
                onDismissRequest = { showTimePicker.value = false },
                properties = DialogProperties(
                    usePlatformDefaultWidth = false
                ),
            ) {
                val timePickerState = rememberTimePickerState()
                Surface(
                    shape = MaterialTheme.shapes.extraLarge,
                    tonalElevation = 6.dp,
                    modifier = Modifier.run {
                        width(IntrinsicSize.Min)
                            .height(IntrinsicSize.Min)
                            .background(
                                shape = MaterialTheme.shapes.extraLarge,
                                color = Color.White
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
                                clickListener = { showTimePicker.value = false },
                                buttonType = ButtonType.Dissmiss
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            ButtonComponent(
                                text = LanguageConstants.DONE_BUTTON_TITLE.localizeString(),
                                clickListener = {
                                    showTimePicker.value = false
                                    val calendar = Calendar.getInstance()
                                    calendar.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                                    calendar.set(Calendar.MINUTE, timePickerState.minute)
                                    selectedTime.value = calendar.time.toFormattedString("hh:mm:ss.sssZ")
                                    formattedSelectedTime.value = calendar.time.toFormattedString("hh:mm a")
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
fun PreviewDateTimePicker() {
    DateTimePickerComponent()
}