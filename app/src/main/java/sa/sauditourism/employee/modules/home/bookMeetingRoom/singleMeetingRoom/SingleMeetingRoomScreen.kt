package sa.sauditourism.employee.modules.home.bookMeetingRoom.singleMeetingRoom

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import sa.sauditourism.employee.R
import sa.sauditourism.employee.components.ButtonComponent
import sa.sauditourism.employee.components.ButtonSize
import sa.sauditourism.employee.components.ButtonType
import sa.sauditourism.employee.components.CustomAlertDialog
import sa.sauditourism.employee.components.CustomTextComponent
import sa.sauditourism.employee.components.DatePickerComponent
import sa.sauditourism.employee.components.DialogType
import sa.sauditourism.employee.components.FooterComponent
import sa.sauditourism.employee.components.GalleryType
import sa.sauditourism.employee.components.HeaderComponent
import sa.sauditourism.employee.components.HeaderModel
import sa.sauditourism.employee.components.InputFieldComponent
import sa.sauditourism.employee.components.InputFieldModel
import sa.sauditourism.employee.components.InputFieldType
import sa.sauditourism.employee.components.MediaComponent
import sa.sauditourism.employee.components.MediaContent
import sa.sauditourism.employee.components.TimePickerComponent
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.formatTimeFromTo
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.modules.BottomNavItem
import sa.sauditourism.employee.modules.PastOrPresentSelectableDates
import sa.sauditourism.employee.modules.activities.participants.AddParticipantSection
import sa.sauditourism.employee.modules.activities.participants.ParticipantSection
import sa.sauditourism.employee.modules.activities.participants.addParticipant.AddParticipantBottomSheet
import sa.sauditourism.employee.modules.home.bookMeetingRoom.model.available_meeting_rooms.Room
import sa.sauditourism.employee.modules.home.bookMeetingRoom.singleMeetingRoom.components.RoomCapacityComponent
import sa.sauditourism.employee.modules.home.bookMeetingRoom.singleMeetingRoom.components.RoomFeaturesComponent
import sa.sauditourism.employee.modules.home.bookMeetingRoom.singleMeetingRoom.components.RoomFloorComponent
import sa.sauditourism.employee.modules.common.ChangeStatusBarIconsColors
import sa.sauditourism.employee.modules.home.bookMeetingRoom.TimeValidationError
import sa.sauditourism.employee.modules.login.Routes
import sa.sauditourism.employee.modules.login.Routes.LOCAL_PARTICIPANTS_SCREEN
import sa.sauditourism.employee.modules.services.model.participants.Participant
import sa.sauditourism.employee.resources.AppColors.ColorsPrimitivesFour
import sa.sauditourism.employee.ui.theme.AppColors
import sa.sauditourism.employee.ui.theme.AppFonts
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleMeetingRoomScreen(
    navController: NavController,
    viewModel: SingleMeetingRoomViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    ) {

    val room = rememberSaveable { navController.previousBackStackEntry?.savedStateHandle?.get<Room>("room")!! }
    val showFromPicker by viewModel.showFromPicker
    val showToPicker by viewModel.showToPicker
    val showDate by viewModel.showDatePicker
    val startTime by viewModel.startTime
    val endTime by viewModel.endTime
    val date by viewModel.selectedDate
    var showAddBottomSheet by remember { mutableStateOf(false) }
    val isEnabled by viewModel.isEnabled
    val selectedParticipants by viewModel.selectedParticipants
    val showError by viewModel.showError
    var errorMessage by remember {
        mutableStateOf("")
    }
    val bookMeetingFlow by viewModel.bookMeetingRoom.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(bookMeetingFlow) {
        if (bookMeetingFlow is UiState.Success) {
            showDialog = false
            navController.navigate(
                Routes.SUCCESSFULLY_BOOKED_SCREEN
            )
        }
        if (bookMeetingFlow is UiState.Error) {
            showDialog = false
        }
    }
    if (showError) {
        showDialog = false
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
    ChangeStatusBarIconsColors(Color.White)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            val headerModel = HeaderModel(
                title = room?.title.orEmpty(),
                subTitle = "",
                showBackIcon = true,
            )
            HeaderComponent(
                headerModel,
                isStandAlone = true,
                backgroundColor = Color.White,
                titleMaxLines = 1,
                showShadow = true,
                backHandler = {
                    onBackClick()
//                    navController.popBackStack()
                    true
                },
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 24.dp, end = 24.dp)
                    .verticalScroll(rememberScrollState()),
            ) {

                Spacer(modifier = Modifier.height(20.dp))
                MediaComponent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(178.dp)
                        .clip(
                            shape = RoundedCornerShape(16.dp)
                        ), mediaContent = MediaContent(
                        GalleryType.IMAGE, room?.image
                    ),
                    contentScale = ContentScale.FillBounds
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    RoomCapacityComponent(
                        capacity = room?.capacity
                    )
                    RoomFloorComponent(
                        floor = room?.floor
                    )
                    RoomFeaturesComponent(features = room?.features)
                }
                Spacer(modifier = Modifier.height(24.dp))
                InputFieldComponent(
                    paddingStart = 16.dp,
                    value = viewModel.roomTitle.value,
                    inputFieldModel = InputFieldModel(
                        placeholderText = LanguageConstants.MEETING_TITLE.localizeString(),
                        type = InputFieldType.Normal,
                    ),
                    isOptional = false,
                    onValueChange = { text ->
                        viewModel.updateRoomTitle(text)
                    },
                )
                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(thickness = 1.dp, color = AppColors.ColorsPrimitivesSix)
                Spacer(modifier = Modifier.height(19.dp))

//                 Parse the date string to LocalDate
                // the added hours is to fix the issue of UTC
                val localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                val dateMillis = localDate.atStartOfDay(ZoneId.systemDefault()).plusHours(5).toInstant()
                    .toEpochMilli()

                val datePickerState = rememberDatePickerState(
                    selectableDates = PastOrPresentSelectableDates,
                    initialSelectedDateMillis = dateMillis
                )
                DatePickerComponent(
                    state = datePickerState,
                    modifier = Modifier,
                    restrictPastDates = true,
                    defaultValue = date,
                    containerHeight = 48.dp,
                    paddingStart = 14.dp,
                    showPicker = showDate,
                    onPickerStateChange = {
                        viewModel.showDatePicker.value = it
                    },
                    onDateSelected = {
                        viewModel.updateSelectedDate(it)
                    },
                    filledBorderColor = ColorsPrimitivesFour,
                    dateViewFormat = "dd-MM-yyyy"

                )
                Spacer(modifier = Modifier.height(16.dp))
                //time pickers row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(37.5.dp),
                    verticalAlignment = Alignment.CenterVertically
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
                        textColor = AppColors.ColorsPrimitivesOne,
                        outputFormat = "HH:mm",
                        filledBorderColor = ColorsPrimitivesFour,
                        error = if (showError) errorMessage else "",
                        showErrorMessage = false,
                        showDefaultTimeOnDialog = true,
                        useSaudiFormatter = false

                    )
                    CustomTextComponent(
                        text = LanguageConstants.TO.localizeString(),
                        style = AppFonts.labelSemiBold,
                        modifier = Modifier.padding(
                            top = 8.dp
                        )
                    )
                    TimePickerComponent(
                        modifier = Modifier.weight(1f),
                        isOptional = false,
                        showPicker = showToPicker,
                        defaultValue = if (endTime.isNotEmpty()) endTime.formatTimeFromTo(
                            "HH:mm", "hh:mm a"
                        ) else "",
                        readOnly = false,
                        onPickerStateChange = { viewModel.showToPicker.value = it },
                        onConfirmRequest = { time -> viewModel.updateEndTime(time) },
                        onDismissRequest = {},
                        placeHolderText = LanguageConstants.TO.localizeString(),
                        containerHeight = 48.dp,
                        textColor = AppColors.ColorsPrimitivesOne,
                        outputFormat = "HH:mm",
                        filledBorderColor = ColorsPrimitivesFour,
                        showErrorMessage = false,
                        error = if (showError) errorMessage else "",
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
                Spacer(modifier = Modifier.height(29.dp))
                HorizontalDivider(thickness = 1.dp, color = AppColors.ColorsPrimitivesFive)
                Spacer(modifier = Modifier.height(24.dp))
                if (selectedParticipants.isEmpty()) {
                    AddParticipantSection(
                        modifier = Modifier.shadow(1.dp, RoundedCornerShape(8.dp)).zIndex(100f),
                        paddingTop = 0.dp
                    ) {
                        showAddBottomSheet = true
                    }
                } else {
                    ParticipantSection(
                        modifier = Modifier.shadow(1.dp, RoundedCornerShape(8.dp)).zIndex(100f),
                        participants = selectedParticipants.map {
                            Participant(
                            )
                        }
                    ) {
                        navController.navigate(LOCAL_PARTICIPANTS_SCREEN)
                    }
                }
                if (showAddBottomSheet)
                    AddParticipantBottomSheet(
                        requestId = room?.id.orEmpty(),
                        locally = true,
                        selectedUsers = viewModel.selectedParticipants.value,
                        onAddUsers = {
                            showAddBottomSheet = false
                            viewModel.updateSelectedParticipants(it)
                        },
                        onDissmess = { showAddBottomSheet = false }) {
                        showAddBottomSheet = false
                    }

                Spacer(modifier = Modifier.height(24.dp))
                InputFieldComponent(
                    paddingStart = 16.dp,
                    value = "",
                    inputFieldModel = InputFieldModel(
                        type = InputFieldType.Normal,
                        maxCharLimit = 100,
                        showCharCounter = true,
                        maxLines = 3,
                        minLines = 3,
                        singleLine = false
                    ),
                    isOptional = true,
                    title = LanguageConstants.ROOM_DESCRIPTION.localizeString(),
                    onValueChange = { text ->
                        viewModel.setDescription(
                            text.trim().ifEmpty { null })
                    },
                )
                Spacer(modifier = Modifier.height(200.dp))
            }


        }
        FooterComponent(
            modifier = Modifier.align(Alignment.BottomCenter),

            ) {
            Column(
                Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .height(138.dp)
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp, top = 16.dp),
                verticalArrangement = Arrangement.Top
            ) {

                ButtonComponent(
                    LanguageConstants.BOOK_ROOM.localizeString(),
                    clickListener = {
                        showDialog = true
                        viewModel.submitForm(room)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    buttonType = ButtonType.PRIMARY,
                    buttonSize = ButtonSize.LARGE,
                    enabled = isEnabled,
                )

                ButtonComponent(
                    LanguageConstants.CANCEL.localizeString(),
                    clickListener = {
                        //to pop up to my day screen
                        navController.popBackStack(
                            BottomNavItem.getTabs()[0].screenRoute,
                            inclusive = false
                        )},
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.3f),
                    buttonType = ButtonType.WHITE_BACKGROUND,
                    buttonSize = ButtonSize.LARGE
                )
            }
        }


        if (showDialog) {
            Dialog(
                onDismissRequest = { },
                DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                ) {
                    CircularProgressIndicator()
                }
            }
        }
        if (bookMeetingFlow is UiState.Error) {
            CustomAlertDialog(
                type = DialogType.Failed,
                imageResId = R.drawable.ic_attention,
                title = LanguageConstants.TIME_TAKEN_ERROR_TITLE.localizeString(),
                description = LanguageConstants.TIME_TAKEN_ERROR_DESCRIPTION.localizeString(),
                primaryButtonText = LanguageConstants.BACK_TO_MY_DAY.localizeString(),
                onPrimaryButtonClick = {
                    navController.popBackStack(
                        BottomNavItem.getTabs()[0].screenRoute,
                        inclusive = false
                    )
                },
                onDismiss = {
                    navController.popBackStack(
                        BottomNavItem.getTabs()[0].screenRoute,
                        inclusive = false
                    )
                },
            )
        }

    }
}

//@Preview
//@Composable
//fun SingleMeetingRoomScreenPreview() {
//    SingleMeetingRoomScreen(
//        navController = rememberNavController(),
//        room = Room(
//            title = "Hello Title",
//            floor = "1",
//            capacity = 6,
//            features = listOf(
//                Feature(
//                    name = "board",
//                    icon = "",
//                ),
//                Feature(
//                    name = "Screen",
//                    icon = "",
//                ),
//                Feature(
//                    name = "Mirrorong"
//                )
//            )
//        ),
//        startTime = "01:00:40.040+0300",
//        endTime = "03:00:46.046+0300",
//        date = "2025-01-08"
//    )
//}