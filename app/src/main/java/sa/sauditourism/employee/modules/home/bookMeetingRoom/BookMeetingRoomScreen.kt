package sa.sauditourism.employee.modules.home.bookMeetingRoom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.ButtonComponent
import sa.sauditourism.employee.components.ButtonSize
import sa.sauditourism.employee.components.ButtonType
import sa.sauditourism.employee.components.CustomAlertDialog
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.DatePickerComponent
import sa.sauditourism.employee.components.DetailsHeaderComponent
import sa.sauditourism.employee.components.DialogType
import sa.sauditourism.employee.components.HeaderModel
import sa.sauditourism.employee.components.TimePickerComponent
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.formatTimeFromTo
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.modules.FutureSelectableDates
import sa.sauditourism.employee.modules.common.ChangeStatusBarIconsColors
import sa.sauditourism.employee.modules.login.Routes
import sa.sauditourism.employee.resources.AppColors
import sa.sauditourism.employee.ui.theme.AppFonts
import java.util.Calendar
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookMeetingRoomScreen(
    navController: NavController,
    viewModel: BookMeetingRoomViewModel = hiltViewModel(),
) {

    val showFromPicker by viewModel.showFromPicker
    val showToPicker by viewModel.showToPicker
    val startTime by viewModel.startTime
    val endTime by viewModel.endTime
    val date by viewModel.selectedDate
    val isEnabled by viewModel.isEnabled
    val showError by viewModel.showError
    var errorMessage by remember {
        mutableStateOf("")
    }

    if (showError) {
        errorMessage = when (viewModel.errorType.value) {
            TimeValidationError.END_BEFORE_START -> LanguageConstants.INVALID_TIME_INTERVAL.localizeString()
            TimeValidationError.INVALID_DURATION -> LanguageConstants.INVALID_MEETING_DURATION.localizeString()
            TimeValidationError.MISSING_FIELDS -> LanguageConstants.UNEXPECTED_ERROR_HAS_OCCURRED.localizeString()
            TimeValidationError.INVALID_FORMAT -> LanguageConstants.UNEXPECTED_ERROR_HAS_OCCURRED.localizeString()
            TimeValidationError.END_TIME_BEFORE_OR_EQUAL_CURRENT -> LanguageConstants.TIME_HAS_ALREADY_PASSED.localizeString()
            TimeValidationError.START_TIME_EQUAL_END_TIME -> LanguageConstants.INVALID_TIME_INTERVAL.localizeString()
            else -> LanguageConstants.UNEXPECTED_ERROR_HAS_OCCURRED.localizeString()
        }
    }
    ChangeStatusBarIconsColors(Color.Transparent)
    Box(
        modifier = Modifier
            .fillMaxSize()
            // added the bottom bars padding here
            .navigationBarsPadding()
    ) {

        Column(
            Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            val headerModel = HeaderModel(
                title = LanguageConstants.BOOK_MEETING_ROOM_TITLE.localizeString(),
                subTitle = "",
                showBackIcon = true,
            )
            DetailsHeaderComponent(
                headerModel,
                isStandAlone = true,
                backgroundColor = Color.Transparent,
                titleMaxLines = 1,
                showShadow = false,
                backHandler = {
                    navController.popBackStack()
                    true
                },
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 24.dp, end = 24.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                val datePickerState =
                    rememberDatePickerState(
                        selectableDates = FutureSelectableDates,
                        initialSelectedDateMillis = Calendar.getInstance(TimeZone.getDefault())
                            .apply {
                                // Ensure we only consider the date part
                                set(Calendar.HOUR_OF_DAY, 5)
                                set(Calendar.MINUTE, 0)
                                set(Calendar.SECOND, 0)
                                set(Calendar.MILLISECOND, 0)
                            }.timeInMillis
                    )
                DatePickerComponent(
                    restrictPastDates = true,
                    isOptional = true,
                    showOnlyPicker = true,
                    state = datePickerState,
                    modifier = Modifier,
                    datePickerShadow = 5.dp,
                    //removed the fixed height here
//                    datePickerHeight = 300.dp
                ) { date ->
                    viewModel.updateSelectedDate(date)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier.height(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CustomTextComponent(

                        text = LanguageConstants.TIME.localizeString(),
                        style = AppFonts.body1Medium,
                        color = Color.Black,
                        lineHeight = 24.sp,
                        textAlign = TextAlign.Start
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                //time pickers row
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TimePickerComponent(
                        modifier = Modifier.weight(1f),
                        isOptional = false,
                        showPicker = showFromPicker,
                        defaultValue = startTime.formatTimeFromTo(
                            "HH:mm", "hh:mm a"
                        ),
                        readOnly = false,
                        onPickerStateChange = { viewModel.showFromPicker.value = it },
                        onConfirmRequest = { time -> viewModel.updateStartTime(time) },
                        onDismissRequest = {},
                        placeHolderText = LanguageConstants.FROM.localizeString(),
                        containerHeight = 48.dp,
                        outputFormat = "HH:mm",
                        filledBorderColor = AppColors.ColorsPrimitivesFour,
                        error = if (showError) errorMessage else "",
                        showErrorMessage = false,
                        showDefaultTimeOnDialog = true,
                        useSaudiFormatter = false

                    )
                    TimePickerComponent(
                        modifier = Modifier.weight(1f),
                        isOptional = false,
                        showPicker = showToPicker,
                        defaultValue = if (endTime.isNotEmpty()) endTime.formatTimeFromTo(
                            "HH:mm", "hh:mm a"
                        ) else "",
                        outputFormat = "HH:mm",
                        readOnly = false,
                        onPickerStateChange = { viewModel.showToPicker.value = it },
                        onConfirmRequest = { time -> viewModel.updateEndTime(time) },
                        onDismissRequest = {},
                        placeHolderText = LanguageConstants.TO.localizeString(),
                        containerHeight = 48.dp,
                        filledBorderColor = AppColors.ColorsPrimitivesFour,
                        error = if (showError) errorMessage else "",
                        showErrorMessage = false,
                        showDefaultTimeOnDialog = true,
                        useSaudiFormatter = false
                    )
                }
                if (showError) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_information_icon),
                            "info",
                            tint = AppColors.ColorFeedbackError,
                            modifier = Modifier
                                .size(16.dp),
                        )
                        CustomTextComponent(
                            text = errorMessage,
                            color = AppColors.ColorFeedbackError,
                            style = AppFonts.tagsSemiBold,
                        )
                    }
                }
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                )
                ButtonComponent(
                    text = LanguageConstants.AVAILABLE_MEETING_ROOM_BUTTON_TITLE.localizeString(),
                    clickListener = {
                        if (!viewModel.checkTimeValidity()) {
                            navController.navigate(
                                "${Routes.AVAILABLE_MEETING_ROOMS}/${date}/${startTime}/${endTime}"
                            )
                        }
                    },
                    enabled = isEnabled,
                    modifier = Modifier
                        .fillMaxWidth(),
                    buttonType = ButtonType.PRIMARY,
                    buttonSize = ButtonSize.LARGE
                )
                Spacer(
                    modifier = Modifier
                        .height(18.dp)
                )
            }
        }
    }
}
