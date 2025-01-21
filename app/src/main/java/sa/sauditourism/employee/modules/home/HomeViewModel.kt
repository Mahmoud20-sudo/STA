package sa.sauditourism.employee.modules.home

import androidx.lifecycle.DefaultLifecycleObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.supervisorScope
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.base.BaseViewModel
import sa.sauditourism.employee.di.SharedPreferencesModule
import sa.sauditourism.employee.extensions.asyncIO
import sa.sauditourism.employee.extensions.launchIO
import sa.sauditourism.employee.managers.network.helpers.NetworkError
import sa.sauditourism.employee.extensions.toFormattedString
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.realtime.RealTimeDatabase
import sa.sauditourism.employee.managers.sharedprefrences.SharedPreferencesManager
import sa.sauditourism.employee.modules.home.domain.HomeRepository
import sa.sauditourism.employee.modules.home.model.AnnouncementResponse
import sa.sauditourism.employee.modules.home.model.QuickActionsResponseModel
import sa.sauditourism.employee.modules.home.model.UserData
import sa.sauditourism.employee.modules.home.myDayMeetings.domain.MyDayMeetingsRepository
import sa.sauditourism.employee.modules.home.myDayMeetings.model.MeetingItem
import sa.sauditourism.employee.modules.home.myDayMeetings.model.MyMeetingsModel
import java.util.Calendar
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    realTimeDatabase: RealTimeDatabase,
    private val myDayMeetingsRepository: MyDayMeetingsRepository,
    private val homeRepository: HomeRepository
) : BaseViewModel(realTimeDatabase), DefaultLifecycleObserver {

    private val _meetingsData =
        MutableStateFlow<UiState<MyMeetingsModel>>(UiState.Loading())
    val meetingsData: StateFlow<UiState<MyMeetingsModel>> = _meetingsData.asStateFlow()

    private val _userData = MutableStateFlow<UiState<UserData>>(UiState.Loading())
    val userData: StateFlow<UiState<UserData>> = _userData.asStateFlow()


    private val _announcement = MutableStateFlow<UiState<AnnouncementResponse>>(UiState.Loading())
    val announcement: StateFlow<UiState<AnnouncementResponse>> = _announcement.asStateFlow()

    private val _quickActinsState =
        MutableStateFlow<UiState<QuickActionsResponseModel>>(UiState.Loading())
    val quickActinsState: StateFlow<UiState<QuickActionsResponseModel>> =
        _quickActinsState.asStateFlow()

    init {
        getUserData()
    }

    fun getAnnouncement() = launchIO {
        homeRepository.getAnnouncement().collect {
            when (it) {
                is UiState.Loading -> {
                    _announcement.emit(UiState.Loading())
                    Timber.d("${ApiNumberCodes.GET_ANNOUNCEMENT}: UiState.Loading")
                }

                is UiState.Success -> {
                    if (it.data?.code != 200) {
                        _announcement.emit(UiState.Error(NetworkError(it.data?.message)))
                        Timber.d("${ApiNumberCodes.GET_ANNOUNCEMENT}: ${it.data?.message}")
                        return@collect
                    }
                    it.data?.data?.let { response ->
                        _announcement.emit(UiState.Success(response))
                    }
                    Timber.d("${ApiNumberCodes.GET_ANNOUNCEMENT}: UiState.ERROR")
                }


                is UiState.Error -> {
                    it.networkError?.let { error ->
                        _announcement.emit(UiState.Error(error))
                        Timber.d("${ApiNumberCodes.GET_ANNOUNCEMENT}: ${error.message}")
                    }
                }
            }
        }
    }

    fun getQuickActions() = launchIO {
        homeRepository.getQuickActions().collect {
            when (it) {
                is UiState.Loading -> {
                    _quickActinsState.emit(UiState.Loading())
                    Timber.d("${ApiNumberCodes.GET_QUICK_ACTIONS}: UiState.Loading")
                }

                is UiState.Success -> {
                    if (it.data?.code != 200) {
                        _quickActinsState.emit(UiState.Error(NetworkError(it.data?.message)))
                        Timber.d("${ApiNumberCodes.GET_QUICK_ACTIONS}: ${it.data?.message}")
                        return@collect
                    }
                    it.data.data?.let { response ->
                        _quickActinsState.emit(UiState.Success(response))
                    }
                    Timber.d("${ApiNumberCodes.GET_QUICK_ACTIONS}: UiState.SUCCESS")
                }

                is UiState.Error -> {
                    it.networkError?.let { error ->
                        _quickActinsState.emit(UiState.Error(error))
                        Timber.d("${ApiNumberCodes.GET_QUICK_ACTIONS}: ${error.message}")
                    }
                }
            }
        }
    }

    fun getMyDayMeetings() = launchIO {
        _meetingsData.emit(UiState.Loading())

        myDayMeetingsRepository.getMyDayMeetings().collect {
            when (it) {
                is UiState.Loading -> {
                    _meetingsData.emit(UiState.Loading())
                    Timber.d("${ApiNumberCodes.MY_DAY_MEETINGS_API_CODE}: UiState.Loading")
                }

                is UiState.Success<*> -> {
                    Timber.d("${ApiNumberCodes.MY_DAY_MEETINGS_API_CODE}: UiState.Success")
                    it.data?.data?.let { response ->
                        val calendar = Calendar.getInstance()
                        val month = calendar.time.toFormattedString("MMM")
                        val day = calendar.time.toFormattedString("dd")

                        var meetingTime = ""
                        val meetingsList = response.meetings?.map { item ->
                            meetingTime =
                                (item?.start?.toFormattedString("HH:mm:ssZ", "hh:mm a")
                                    ?: "") + " - " +
                                        (item?.end?.toFormattedString("HH:mm:ssZ", "hh:mm a") ?: "")

                            MeetingItem(
                                title = item?.subject ?: "",
                                time = meetingTime
                            )
                        } ?: listOf()

                        _meetingsData.emit(
                            UiState.Success(
                                MyMeetingsModel(
                                    month = month,
                                    day = day,
                                    meetingsList = meetingsList
                                )
                            )
                        )
                    }
                }

                is UiState.Error -> {
                    it.networkError?.let { error ->
                        _meetingsData.emit(UiState.Error(error))
                        Timber.d("${ApiNumberCodes.MY_DAY_MEETINGS_API_CODE}: ${error.message}")
                    }
                }
            }
        }
    }

    private fun getUserData() = launchIO {
        _userData.emit(UiState.Loading())
        val sharedPreferencesManager: SharedPreferencesManager =
            SharedPreferencesModule.provideSharedPreferencesManager(EmployeeApplication.instance.applicationContext)

        val userName = sharedPreferencesManager.userName
        val image = sharedPreferencesManager.userImage

        _userData.emit(UiState.Success(UserData(userName, image)))
    }

    fun clearMeetingState() = launchIO {
        _meetingsData.emit(UiState.Loading())
    }

    fun clearAnnouncementState() = launchIO {
        _announcement.emit(UiState.Loading())
    }

    fun clearActionsState() = launchIO {
        _quickActinsState.emit(UiState.Loading())
    }
}