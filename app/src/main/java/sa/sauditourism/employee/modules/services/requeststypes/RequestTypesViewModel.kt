package sa.sauditourism.employee.modules.services.requeststypes

import androidx.lifecycle.DefaultLifecycleObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import sa.sauditourism.employee.base.BaseViewModel
import sa.sauditourism.employee.extensions.launchIO
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.realtime.RealTimeDatabase
import sa.sauditourism.employee.modules.services.domain.RequestTypeRepository
import sa.sauditourism.employee.modules.services.model.RequestTypesModel
import timber.log.Timber
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class RequestsTypesViewModel @Inject constructor(
    private val requestTypeRepository: RequestTypeRepository,
    realTimeDatabase: RealTimeDatabase,
)  : BaseViewModel(realTimeDatabase), DefaultLifecycleObserver {

    private val _requestsTypes = MutableStateFlow<UiState<RequestTypesModel>>(UiState.Loading())
    val requestsTypes: StateFlow<UiState<RequestTypesModel>> = _requestsTypes.asStateFlow()


    fun searchRequestsTypes(serviceId: String, query: String) = launchIO {
        requestTypeRepository.searchRequestTypesById(serviceId, query).collectLatest {
            when (it) {
                is UiState.Loading -> {
                    _requestsTypes.emit(UiState.Loading())
                    Timber.d("${ApiNumberCodes.REQUESTS_TYPES_API_CODE}: UiState.Loading")
                }

                is UiState.Success<*> -> {
                    Timber.d("${ApiNumberCodes.REQUESTS_TYPES_API_CODE}: UiState.Success")
                    it.data?.data?.let { response ->
                        _requestsTypes.emit(
                            UiState.Success(
                                RequestTypesModel(
                                    response.requestsTypesList.toMutableList(),
                                    mutableListOf()
                                )

                            ))
                    }
                }

                is UiState.Error -> {
                    it.networkError?.let { error ->
                        _requestsTypes.emit(UiState.Error(error))
                        Timber.d("${ApiNumberCodes.REQUESTS_TYPES_API_CODE}: ${error.message}")
                    }
                }
            }
        }
    }
    fun filterRequestsType(query: String) = launchIO {

        val filterList =requestsTypes.value.data?.requestsTypeList?.filter {
               it.title.contains(query, ignoreCase = true)
        }
        _requestsTypes.emit(
            UiState.Success(
                RequestTypesModel(
                    requestsTypeList = requestsTypes.value.data?.requestsTypeList ?: mutableListOf(),
                    filterRequestsTypeList = filterList?.toMutableList() ?: mutableListOf()
                )
        )
        )
    }

    fun searchRequestsTypes(query: String) = launchIO {

        requestTypeRepository.searchAllRequestTypes(query).debounce(timeoutMillis = 500).collectLatest {
            when (it) {
                is UiState.Loading -> {
                    _requestsTypes.emit(UiState.Loading())
                    Timber.d("${ApiNumberCodes.REQUESTS_TYPES_API_CODE}: UiState.Loading")
                }

                is UiState.Success<*> -> {
                    Timber.d("${ApiNumberCodes.REQUESTS_TYPES_API_CODE}: UiState.Success")
                    it.data?.data?.let { response ->
                        _requestsTypes.emit(
                            UiState.Success(
                                RequestTypesModel(
                                    response.requestsTypesList.toMutableList(),
                                    mutableListOf()
                                )
                            ))
                    }
                }

                is UiState.Error -> {
                    it.networkError?.let { error ->
                        _requestsTypes.emit(UiState.Error(error))
                        Timber.d("${ApiNumberCodes.REQUESTS_TYPES_API_CODE}: ${error.message}")
                    }
                }
            }
        }
    }

    override fun clearState() = launchIO { _requestsTypes.emit(UiState.Loading()) }
}