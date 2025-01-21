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
import sa.sauditourism.employee.modules.account.domain.PaySlipsRepository
import sa.sauditourism.employee.modules.account.model.AccountDetails
import sa.sauditourism.employee.modules.account.model.PaySlipsResponseModel
import sa.sauditourism.employee.modules.services.model.RequestsType
import sa.sauditourism.employee.modules.services.model.ServicesResponseModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class PaySlipsViewModel @Inject constructor(
    private val paySlipsRepository: PaySlipsRepository,
    realTimeDatabase: RealTimeDatabase,
) : BaseViewModel(realTimeDatabase), DefaultLifecycleObserver {

    private val _paySlipsFlow =
        MutableStateFlow<UiState<PaySlipsResponseModel>>(UiState.Loading())
    val paySlipsFlow: StateFlow<UiState<PaySlipsResponseModel>> = _paySlipsFlow.asStateFlow()

    fun getPaySlips(date: String? = null) = launchIO {
        paySlipsRepository.getPaySlips(date).collect {
            when (it) {
                is UiState.Loading -> {
                    _paySlipsFlow.emit(UiState.Loading())
                    Timber.d("${ApiNumberCodes.PAYSLIPS_API_CODE}: UiState.Loading")
                }

                is UiState.Success<*> -> {
                    if (it.data?.code != 200) {
                        _paySlipsFlow.emit(UiState.Error(NetworkError(message = it.data?.message)))
                        return@collect
                    }
                    it.data.data?.let { response ->
                        _paySlipsFlow.emit(UiState.Success(response))
                    }
                    Timber.d("${ApiNumberCodes.PAYSLIPS_API_CODE}: UiState.Success")
                }

                is UiState.Error -> {
                    it.networkError?.let { error ->
                        _paySlipsFlow.emit(UiState.Error(error))
                        Timber.d("${ApiNumberCodes.PAYSLIPS_API_CODE}: ${error.message}")
                    }
                }
            }
        }
    }
}
