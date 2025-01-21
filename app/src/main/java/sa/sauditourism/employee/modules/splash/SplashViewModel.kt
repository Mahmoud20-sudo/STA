package sa.sauditourism.employee.modules.splash

import androidx.activity.ComponentActivity
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.MainViewModel.SSIDEvent
import sa.sauditourism.employee.base.BaseViewModel
import sa.sauditourism.employee.di.RealTimeDatabaseModule
import sa.sauditourism.employee.di.SharedPreferencesModule
import sa.sauditourism.employee.extensions.launchIO
import sa.sauditourism.employee.managers.environment.EnvironmentManager
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.helpers.NetworkError
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.realtime.RealTimeDatabase
import sa.sauditourism.employee.managers.remoteconfig.RemoteConfigService
import sa.sauditourism.employee.managers.sharedprefrences.SharedPreferencesManager
import sa.sauditourism.employee.modules.account.domain.AccountRepository
import sa.sauditourism.employee.modules.account.model.AccountResponse
import sa.sauditourism.employee.modules.onboarding.model.OnBoarding
import sa.sauditourism.employee.modules.onboarding.model.OnBoardingModel
import sa.sauditourism.employee.modules.services.model.ServicesModel
import sa.sauditourism.employee.modules.splash.model.AccountTabModel
import sa.sauditourism.employee.modules.splash.model.AppMaintenanceModel
import sa.sauditourism.employee.modules.splash.model.AppUpdateModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    realTimeDatabase: RealTimeDatabase
) : BaseViewModel(realTimeDatabase) {

    private val _languageFetched: MutableStateFlow<Boolean?> =
        MutableStateFlow(null)
    val languageFetched: StateFlow<Boolean?> = _languageFetched.asStateFlow()

    private val _appUpdateFetched: MutableStateFlow<AppUpdateModel?> =
        MutableStateFlow(null)
    val appUpdateFetched: StateFlow<AppUpdateModel?> = _appUpdateFetched.asStateFlow()

    private val _appMaintenanceFetched: MutableStateFlow<AppMaintenanceModel?> =
        MutableStateFlow(null)
    val appMaintenanceFetched: StateFlow<AppMaintenanceModel?> =
        _appMaintenanceFetched.asStateFlow()

    private val _appconfigurationsFetched: MutableStateFlow<Boolean?> =
        MutableStateFlow(null)
    val appconfigurationsFetched: StateFlow<Boolean?> =
        _appconfigurationsFetched.asStateFlow()

    private val _startDelayFetched: MutableStateFlow<Boolean?> =
        MutableStateFlow(null)
    val startDelayFetched: StateFlow<Boolean?> = _startDelayFetched.asStateFlow()

    private val _servicesFetched: MutableStateFlow<List<ServicesModel>?> =
        MutableStateFlow(null)
    val servicesFetched: StateFlow<List<ServicesModel>?> =
        _servicesFetched.asStateFlow()

    private val _onBoardingFetched: MutableStateFlow<List<OnBoarding>?> =
        MutableStateFlow(null)
    val onBoardingFetched: StateFlow<List<OnBoarding>?> =
        _onBoardingFetched.asStateFlow()

    fun initSplash() {
        viewModelScope.launch {

            val realTimeDatabaseManager =
                RealTimeDatabaseModule.provideRealTimeDatabaseManager()

            realTimeDatabaseManager.authenticateRealTimeDb()

            realTimeDatabaseManager.isConnectedToDb.collect {
                when (it) {
                    true -> {
                        realTimeDatabaseManager.fetchDataOnInitialization(
                            _languageFetched,
                            _appUpdateFetched,
                            _appMaintenanceFetched,
                            _appconfigurationsFetched,
                            _servicesFetched,
                            _onBoardingFetched
                        )
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(1000)
                            _startDelayFetched.value = true
                        }
                    }

                    false -> {
                        _languageFetched.emit(false)
                        _appUpdateFetched.emit(AppUpdateModel())
                        _appMaintenanceFetched.emit(AppMaintenanceModel())
                        _appconfigurationsFetched.emit(false)
                        _servicesFetched.emit(emptyList())
                        _onBoardingFetched.emit(emptyList())

                        CoroutineScope(Dispatchers.Main).launch {
                            delay(1000)
                            _startDelayFetched.value = true
                        }
                    }

                    else -> {
                        _startDelayFetched.value = true
                    }
                }
            }
        }
    }

    fun checkChinaCountry(activity: ComponentActivity) {
        viewModelScope.launch {
            RemoteConfigService.fetchRemoteConfig(activity)
        }
    }

    override fun onSwipeToRefresh() {

    }

}
