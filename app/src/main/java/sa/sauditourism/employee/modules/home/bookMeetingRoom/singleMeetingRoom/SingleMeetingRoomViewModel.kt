package sa.sauditourism.employee.modules.home.bookMeetingRoom.singleMeetingRoom

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import sa.sauditourism.employee.base.BaseViewModel
import sa.sauditourism.employee.components.DropDownModel
import sa.sauditourism.employee.extensions.launchIO
import sa.sauditourism.employee.extensions.toRiyadhTime
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.helpers.NetworkError
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.realtime.RealTimeDatabase
import sa.sauditourism.employee.modules.home.bookMeetingRoom.TimeValidationError
import sa.sauditourism.employee.modules.home.bookMeetingRoom.domain.BookMeetingRoomRepository
import sa.sauditourism.employee.modules.home.bookMeetingRoom.model.available_meeting_rooms.Room
import sa.sauditourism.employee.modules.home.bookMeetingRoom.model.single_meeting_room.request.BookMeetingRoomRequestModel
import sa.sauditourism.employee.modules.home.bookMeetingRoom.model.single_meeting_room.response.BookMeetingRoomResponse
import timber.log.Timber
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
class SingleMeetingRoomViewModel @Inject constructor(
    realTimeDatabase: RealTimeDatabase,
    savedStateHandle: SavedStateHandle,
    private val bookMeetingRoomRepository: BookMeetingRoomRepository
) : BaseViewModel(realTimeDatabase), DefaultLifecycleObserver {


    private val _bookMeetingRoomFlow =
        MutableStateFlow<UiState<BookMeetingRoomResponse>>(UiState.Loading())
    val bookMeetingRoom: StateFlow<UiState<BookMeetingRoomResponse>> =
        _bookMeetingRoomFlow.asStateFlow()

    var showFromPicker = mutableStateOf(false)
    var showToPicker = mutableStateOf(false)
    var showDatePicker = mutableStateOf(false)
    var startTime = mutableStateOf(savedStateHandle.get<String>("start_time") ?: "")
    var endTime = mutableStateOf(savedStateHandle.get<String>("end_time") ?: "")
    var roomTitle = mutableStateOf("")
    var description = mutableStateOf("")
    var isEnabled = mutableStateOf(false)
    var selectedDate = mutableStateOf(
        savedStateHandle.get<String>("date") ?: ""
    )
    var selectedParticipants = mutableStateOf(
        listOf<DropDownModel>(
        )
    )

    var selectedRoom: Room? = null

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

    fun updateRoomTitle(title: String) {
        roomTitle.value = title
        updateIsEnabled()
    }

    private fun updateIsEnabled() {
        isEnabled.value =
            startTime.value.isNotEmpty() && endTime.value.isNotEmpty() && selectedDate.value.isNotEmpty() && roomTitle.value.isNotEmpty() && selectedParticipants.value.isNotEmpty()
    }

    fun updateSelectedParticipants(participants: List<DropDownModel>) {
        selectedParticipants.value = participants
        updateIsEnabled()
    }

    fun setDescription(desc: String?) {
        description.value = desc.orEmpty()
    }

    fun submitForm(room: Room?) {
        selectedRoom = room
        launchIO {
            if (!checkTimeValidity()) {
                val bookMeetingRoomRequestModel = BookMeetingRoomRequestModel(
                    start = startTime.value.toRiyadhTime("HH:mm"),
                    end = endTime.value.toRiyadhTime("HH:mm"),
                    title = roomTitle.value,
                    body = description.value,
                    date = selectedDate.value,
                    attendees = selectedParticipants.value.map { it.extra }
                )

                bookMeetingRoomRepository.bookMeeting(
                    meetingId = room?.id ?: "",
                    bookMeetRequestModel = bookMeetingRoomRequestModel
                ).collect {
                    when (it) {
                        is UiState.Loading -> {
                            _bookMeetingRoomFlow.emit(UiState.Loading())
                            Timber.d("${ApiNumberCodes.SUBMIT_FORM_API_CODE}: UiState.Loading")
                        }

                        is UiState.Success<*> -> {
                            Timber.d("${ApiNumberCodes.SUBMIT_FORM_API_CODE}: UiState.Success")
                            it.data?.data?.let { response ->
                                _bookMeetingRoomFlow.emit(
                                    if (it.data.code == 200) UiState.Success(response) else UiState.Error(
                                        NetworkError(
                                            apiNumber = ApiNumberCodes.SUBMIT_FORM_API_CODE,
                                            message = kotlin.runCatching {
                                                it.data.data.errors?.get(
                                                    0
                                                )?.errorMessage
                                            }
                                                .getOrElse { _ -> it.data.message }
                                        )
                                    ))
                            }
                        }

                        is UiState.Error -> {
                            it.networkError?.also { error ->
                                _bookMeetingRoomFlow.emit(UiState.Error(error))
                                Timber.d("${ApiNumberCodes.SUBMIT_FORM_API_CODE}: ${error.message}")
                            } ?: run {
                                _bookMeetingRoomFlow.emit(UiState.Error(NetworkError()))
                            }
                        }
                    }
                }


            }
        }
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

    fun removeParticipant(it: String) {
        selectedParticipants.value = selectedParticipants.value.filter { participant ->
            participant.label != it
        }
    }


}