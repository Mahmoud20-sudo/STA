package sa.sauditourism.employee.modules.services

import androidx.lifecycle.DefaultLifecycleObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import sa.sauditourism.employee.base.BaseViewModel
import sa.sauditourism.employee.extensions.launchIO
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.realtime.RealTimeDatabase
import sa.sauditourism.employee.modules.services.domain.ServicesRepository
import sa.sauditourism.employee.modules.services.model.ServicesModel
import sa.sauditourism.employee.modules.services.model.ServicesResponseModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ServicesViewModel @Inject constructor(
    private val servicesRepository: ServicesRepository,
    realTimeDatabase: RealTimeDatabase
) : BaseViewModel(realTimeDatabase), DefaultLifecycleObserver {

    private val _servicesFlow =
        MutableStateFlow<UiState<ServicesResponseModel>>(UiState.Loading())
    val servicesFlow: StateFlow<UiState<ServicesResponseModel>> = _servicesFlow.asStateFlow()


    init {
        getServices()
    }

    fun getServices() = launchIO {
        _servicesFlow.emit(UiState.Loading())

        servicesRepository.getServices().collect {
            when (it) {
                is UiState.Loading -> {
                    _servicesFlow.emit(UiState.Loading())
                    Timber.d("${ApiNumberCodes.SERVICES_API_CODE}: UiState.Loading")
                }

                is UiState.Success<*> -> {
                    Timber.d("${ApiNumberCodes.SERVICES_API_CODE}: UiState.Success")
                    it.data?.data?.let { response ->
                        _servicesFlow.emit(UiState.Success(response))
                    }
                }

                is UiState.Error -> {
                    it.networkError?.let { error ->
                        _servicesFlow.emit(UiState.Error(error))
                        Timber.d("${ApiNumberCodes.SERVICES_API_CODE}: ${error.message}")
                    }
                }
            }
        }
    }
}
