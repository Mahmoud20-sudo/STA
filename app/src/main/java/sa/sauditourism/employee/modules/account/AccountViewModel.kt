package sa.sauditourism.employee.modules.account

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.base.BaseViewModel
import sa.sauditourism.employee.di.SharedPreferencesModule
import sa.sauditourism.employee.extensions.launch
import sa.sauditourism.employee.extensions.launchIO
import sa.sauditourism.employee.managers.environment.EnvironmentKeys
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.helpers.ApiResult
import sa.sauditourism.employee.managers.network.helpers.NetworkError
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.realtime.RealTimeDatabase
import sa.sauditourism.employee.managers.sharedprefrences.SharedPreferencesManager
import sa.sauditourism.employee.modules.account.domain.AccountRepository
import sa.sauditourism.employee.modules.account.domain.ProfileImageRepository
import sa.sauditourism.employee.modules.account.model.AccountDetails
import sa.sauditourism.employee.modules.account.model.DeleteProfileResponseModel
import sa.sauditourism.employee.modules.account.model.PaySlipsResponseModel
import sa.sauditourism.employee.modules.services.model.RequestsType
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    realTimeDatabase: RealTimeDatabase,
    private val accountRepository: AccountRepository,
    private val profileImageRepository: ProfileImageRepository
) : BaseViewModel(realTimeDatabase), DefaultLifecycleObserver {

    private val _event = MutableStateFlow<UiState<AccountDetails>>(UiState.Loading())
    val event: StateFlow<UiState<AccountDetails>> = _event.asStateFlow()

    private val _eventDelete = MutableStateFlow<DeleteProfileEvent?>(null)
    val eventDelete: StateFlow<DeleteProfileEvent?> = _eventDelete.asStateFlow()

    private val _redirectToSaudiRewards = MutableStateFlow(false)
    val redirectToSaudiRewards: StateFlow<Boolean> = _redirectToSaudiRewards.asStateFlow()

    private val _imageDeleteStat =
        MutableStateFlow<UiState<DeleteProfileResponseModel>>(UiState.Loading())
    val imageDeleteStat: StateFlow<UiState<DeleteProfileResponseModel>> = _imageDeleteStat.asStateFlow()

    override fun clearState() = launchIO {
        _imageDeleteStat.emit(UiState.Loading())
    }

    fun updateRedirect(flag: Boolean) {
        _redirectToSaudiRewards.value = flag
    }

    fun getProfile(shouldRefresh: Boolean = true, callLoyaltyApis: Boolean = false) =
        viewModelScope.launch {
            val sharedPreferencesManager: SharedPreferencesManager =
                SharedPreferencesModule.provideSharedPreferencesManager(EmployeeApplication.instance.applicationContext)
            _event.emit(UiState.Loading())
            accountRepository.getAccountDetails("gshamrani@sta.gov.sa").collect {
                if (it is UiState.Success) {
                    it.data?.data?.let { accountDetails ->
                        _event.emit(UiState.Success(accountDetails.accountItem))
                        sharedPreferencesManager.accountDetails = accountDetails.accountItem
                    }
                }
            }


//        if (sharedPreferencesManager.accessToken.isNullOrEmpty() || EmployeeApplication.instance.environmentManager.getVariable(
//                EnvironmentKeys.BASE_URL_V2).isEmpty()) {
//            return
//        }

        }

    fun deleteProfileImage(personId: String) = launchIO {
        profileImageRepository.deleteImage(personId).collect {
            when (it) {
                is UiState.Loading -> {
                    _imageDeleteStat.emit(UiState.Loading())
                    Timber.d("${ApiNumberCodes.DELETE_PROFILE_IMAGE_API_CODE}: UiState.Loading")
                }

                is UiState.Success<*> -> {
                    if (it.data?.code != 200) {
                        _imageDeleteStat.emit(UiState.Error(NetworkError(message = it.data?.message)))
                        return@collect
                    }
                    it.data.data?.let { response ->
                        _event.value.data?.image = response.profilePicture
                        _imageDeleteStat.emit(UiState.Success(response))
                    }
                    Timber.d("${ApiNumberCodes.DELETE_PROFILE_IMAGE_API_CODE}: UiState.Success")
                }

                is UiState.Error -> {
                    it.networkError?.let { error ->
                        _imageDeleteStat.emit(UiState.Error(error))
                        Timber.d("${ApiNumberCodes.DELETE_PROFILE_IMAGE_API_CODE}: ${error.message}")
                    }
                }
            }
        }
    }

    fun updateProfileImage(personId: String, file: File) = launchIO {
        profileImageRepository.updateImage(personId, file).collect {
            when (it) {
                is UiState.Loading -> {
                    _imageDeleteStat.emit(UiState.Loading())
                    Timber.d("${ApiNumberCodes.UPDATE_PROFILE_IMAGE_API_CODE}: UiState.Loading")
                }

                is UiState.Success<*> -> {
                    if (it.data?.code != 200) {
                        _imageDeleteStat.emit(UiState.Error(NetworkError(message = it.data?.message)))
                        return@collect
                    }
                    it.data.data?.let { response ->
                        _event.value.data?.image = response.profilePicture
                        _imageDeleteStat.emit(UiState.Success(response))
                    }
                    Timber.d("${ApiNumberCodes.UPDATE_PROFILE_IMAGE_API_CODE}: UiState.Success")
                }

                is UiState.Error -> {
                    it.networkError?.let { error ->
                        _imageDeleteStat.emit(UiState.Error(error))
                        Timber.d("${ApiNumberCodes.UPDATE_PROFILE_IMAGE_API_CODE}: ${error.message}")
                    }
                }
            }
        }
    }

    sealed class ProfileEvent {
        class ShowData(val response: AccountDetails?) : ProfileEvent()
        class Error(val error: NetworkError?, val response: AccountDetails?) :
            ProfileEvent()

        object Loading : ProfileEvent()
    }

    sealed class DeleteProfileEvent {
        //        class ShowData(val response: UpdateVerifyMobileBean?) : DeleteProfileEvent()
        class Error(val error: NetworkError?, val response: MainResponseModel<Unit>?) :
            DeleteProfileEvent()

        object Loading : DeleteProfileEvent()
    }


    private fun getLoyaltyCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("Asia/Riyadh")
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }


    private var generateHYUserIDJob: Job? = null


    private var hyUserId: MutableLiveData<ApiResult<ResponseBody>> = MutableLiveData()
    fun getHyUserIdResult(): LiveData<ApiResult<ResponseBody>> {
        return hyUserId
    }


    private var generateQrCode: Job? = null


    override fun onSwipeToRefresh() {
        getProfile(true)
    }


}
