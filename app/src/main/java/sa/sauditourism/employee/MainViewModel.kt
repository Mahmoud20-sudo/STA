package sa.sauditourism.employee

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.viewModelScope
import sa.sauditourism.employee.base.BaseViewModel
import sa.sauditourism.employee.modules.login.domain.SSIDRepository
import sa.sauditourism.employee.managers.network.helpers.NetworkError
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.realtime.RealTimeDatabase
import sa.sauditourism.employee.managers.sharedprefrences.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import sa.sauditourism.employee.di.SharedPreferencesModule
import sa.sauditourism.employee.extensions.launchIO
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.modules.account.domain.AccountRepository
import sa.sauditourism.employee.modules.account.model.AccountResponse
import sa.sauditourism.employee.modules.login.authintication.AuthenticationManager
import sa.sauditourism.employee.modules.login.domain.ProfileRepository
import sa.sauditourism.employee.network.status.NetworkConnectivityService
import sa.sauditourism.employee.network.status.model.NetworkStatus
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val networkConnectivityService: NetworkConnectivityService,
//    private val profileRepository: ProfileRepository,
    private val accountRepository: AccountRepository,
    realTimeDatabase: RealTimeDatabase
) : BaseViewModel(realTimeDatabase), DefaultLifecycleObserver {

    private var isDarkThemeEnabled = MutableSharedFlow<Boolean>()

    private val _event = MutableStateFlow<SSIDEvent>(SSIDEvent.Idle)
    val event: StateFlow<SSIDEvent> = _event.asStateFlow()

    private val _eventLogout = MutableStateFlow<SSIDEvent>(SSIDEvent.Idle)
    val eventLogout: StateFlow<SSIDEvent> = _eventLogout.asStateFlow()

    private val _profileFetched: MutableStateFlow<UiState<MainResponseModel<AccountResponse>>> =
        MutableStateFlow(UiState.Loading())
    val profileFetched: StateFlow<UiState<MainResponseModel<AccountResponse>>> =
        _profileFetched.asStateFlow()

    val networkStatus: StateFlow<NetworkStatus> = networkConnectivityService.networkStatus.stateIn(
        initialValue = NetworkStatus.Unknown,
        scope = viewModelScope,
        started = WhileSubscribed(5000)
    )

    fun changeTheme(isDarkTheme: Boolean) {
        viewModelScope.launch {
            sharedPreferencesManager.darkMode = isDarkTheme
            isDarkThemeEnabled.emit(isDarkTheme)
        }
    }

    fun doLogin(isLogin: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        _event.emit(if (isLogin) SSIDEvent.Loading else SSIDEvent.Idle)

        if (isLogin)
            AuthenticationManager.openAuthWeb()
    }

    override fun doLogout() = viewModelScope.launch {
        super.doLogout()
        isProfileFetched = false
        _event.emit(SSIDEvent.Error(NetworkError(), response = null))
    }

    private var isProfileFetched = false

    fun getProfile(userName: String? = null, jiraAccessToken: String? = null) = launchIO {
        if (!isProfileFetched)
            accountRepository.getAccountDetails("gshamrani@sta.gov.sa").collect {
                when (it) {
                    is UiState.Loading -> {
                        isProfileFetched = true
                        _profileFetched.emit(UiState.Loading())
                        Timber.d("${ApiNumberCodes.PROFILE_DATA}: UiState.Loading")
                    }

                    is UiState.Success -> {
                        if (it.data?.code != 200) {
                            _profileFetched.emit(UiState.Error(NetworkError(it.data?.message)))
                            return@collect
                        }
                        it.data.data?.let { accountDetails ->
                            sharedPreferencesManager.accountDetails = accountDetails.accountItem
                            jiraAccessToken?.let { token -> authorizeUser(token) }
                            _profileFetched.emit(UiState.Success(it.data))
                        }
                        Timber.d("${ApiNumberCodes.PROFILE_DATA}: UiState.SUCCESS")
                    }

                    is UiState.Error -> {
                        it.networkError?.let { error ->
                            _profileFetched.emit(UiState.Error(error))
                            Timber.d("${ApiNumberCodes.PROFILE_DATA}: ${error.message}")
                        }
                    }
                }
            }
    }

    override fun clearState() = launchIO {
        _profileFetched.emit(UiState.Loading())
    }

//    fun getProfileData(jiraAccessToken: String) = viewModelScope.launch {
//        _event.emit(SSIDEvent.Loading)
//        profileRepository.getUserData().collect {
//            if (it is UiState.Success) {
//                sharedPreferencesManager.userName = it.data?.data?.name.toString()
//                getProfile(it.data?.data?.name.toString(), jiraAccessToken)
//            }
//        }
//    }

    fun authorizeUser(accessToken: String) = viewModelScope.launch {
        _event.emit(SSIDEvent.ShowData(accessToken))
    }

    fun emilFailState(exception: Exception) = viewModelScope.launch {
        _event.emit(
            SSIDEvent.Error(
                error = NetworkError(message = exception.message),
                response = null
            )
        )
    }

    sealed class SSIDEvent {
        class ShowData(val response: Any?) : SSIDEvent()
        class Error(val error: NetworkError?, val response: Any?) :
            SSIDEvent()

        data object Loading : SSIDEvent()
        data object Idle : SSIDEvent()

    }


    override fun onSwipeToRefresh() {

    }
}
