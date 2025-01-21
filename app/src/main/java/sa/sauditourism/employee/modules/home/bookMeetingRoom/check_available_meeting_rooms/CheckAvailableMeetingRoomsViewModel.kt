package sa.sauditourism.employee.modules.home.bookMeetingRoom.check_available_meeting_rooms

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import sa.sauditourism.employee.base.BaseViewModel
import sa.sauditourism.employee.extensions.launchIO
import sa.sauditourism.employee.extensions.toRiyadhTime
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.helpers.NetworkError
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.realtime.RealTimeDatabase
import sa.sauditourism.employee.modules.home.bookMeetingRoom.domain.AvailableMeetingRoomsRepository
import sa.sauditourism.employee.modules.home.bookMeetingRoom.model.available_meeting_rooms.AvailableMeetingRoomsResponseModel
import sa.sauditourism.employee.modules.home.bookMeetingRoom.model.available_meeting_rooms.Feature
import sa.sauditourism.employee.modules.home.bookMeetingRoom.model.available_meeting_rooms.MeetingRoom
import sa.sauditourism.employee.modules.home.bookMeetingRoom.model.available_meeting_rooms.Room
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel

class CheckAvailableMeetingRoomsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val checkAvailableMeetingRoomsRepository: AvailableMeetingRoomsRepository,
    realTimeDatabase: RealTimeDatabase

) : BaseViewModel(realTimeDatabase), DefaultLifecycleObserver {

    private var date: String = ""
    private var startTime: String = ""
    private var endTime: String = ""

    private val _availableMeetingRoomsFlow =
        MutableStateFlow<UiState<AvailableMeetingRoomsResponseModel>>(UiState.Loading())
    val meetingRoomsFlow: StateFlow<UiState<AvailableMeetingRoomsResponseModel>> =
        _availableMeetingRoomsFlow.asStateFlow()


    // Store the original data for local filtering
    var originalMeetingRooms: AvailableMeetingRoomsResponseModel? = null


    init {
        date = savedStateHandle.get<String>("date") ?: ""
        startTime = savedStateHandle.get<String>("start_time") ?: ""
        endTime = savedStateHandle.get<String>("end_time") ?: ""
        getAvailableMeetingRoom()
    }

    fun getAvailableMeetingRoom(
        query:String?=null
    ) = launchIO {
        _availableMeetingRoomsFlow.emit(UiState.Loading())
        checkAvailableMeetingRoomsRepository.getAvailableMeetingRooms(
            date,
            startTime.toRiyadhTime("HH:mm"),
            endTime.toRiyadhTime("HH:mm")
        ).collect {
            when (it) {
                is UiState.Loading -> {
                    _availableMeetingRoomsFlow.emit(UiState.Loading())
                    Timber.d("${ApiNumberCodes.SERVICES_API_CODE}: UiState.Loading")
                }

                is UiState.Success<*> -> {
                    if (it.data == null || it.data.code != 200) {
                        _availableMeetingRoomsFlow.emit(
                            UiState.Error(
                                NetworkError(

                                )
                            )
                        )
                    } else {
                        Timber.d("${ApiNumberCodes.SERVICES_API_CODE}: UiState.Success")
                        it.data.data?.let { response ->
                            // Store the original data for future filtering
                            originalMeetingRooms = response
                            _availableMeetingRoomsFlow.emit(UiState.Success(response))
                            if (!query.isNullOrEmpty()){
                                filterMeetings(query)
                            }

                        }
                    }

                }

                is UiState.Error -> {
                    it.networkError?.let { error ->
                        _availableMeetingRoomsFlow.emit(UiState.Error(error))
                        Timber.d("${ApiNumberCodes.SERVICES_API_CODE}: ${error.message}")
                    }
                }
            }
        }
    }

    // Local filtering function
    fun filterMeetings(searchQuery: String) = launchIO {
        if (searchQuery.isEmpty()) {
            // Reset to the original data if the search query is empty
            originalMeetingRooms?.let { originalData ->
                _availableMeetingRoomsFlow.emit(UiState.Success(originalData))
            }
        } else {
            // Filter data based on search query
            val currentState = _availableMeetingRoomsFlow.value
            if (currentState is UiState.Success) {
                val response = originalMeetingRooms
                val filteredMeetingRooms = response?.meetingRooms?.map { meetingRoom ->
                    meetingRoom.copy(
                        rooms = meetingRoom.rooms.filter { room ->
                            room.title.contains(searchQuery, ignoreCase = true) ||
                                    room.email.contains(searchQuery, ignoreCase = true)
                        }
                    )
                }?.filter { it.rooms.isNotEmpty() } // Remove floors with no matching rooms

                // Emit the filtered results
                if (response != null) {
                    _availableMeetingRoomsFlow.emit(
                        UiState.Success(
                            response.copy(
                                meetingRooms = filteredMeetingRooms ?: listOf()
                            )
                        )
                    )
                }
            }
        }
    }

    // Clear filter and reset to original data
    fun clearFilter() {
        originalMeetingRooms?.let { originalData ->
            _availableMeetingRoomsFlow.value = UiState.Success(originalData)
        }
    }
}