package sa.sauditourism.employee.modules.account.myRequests

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.supervisorScope
import sa.sauditourism.employee.base.BaseViewModel
import sa.sauditourism.employee.components.FilterModel
import sa.sauditourism.employee.extensions.asyncIO
import sa.sauditourism.employee.extensions.launchIO
import sa.sauditourism.employee.managers.network.helpers.NetworkError
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.realtime.RealTimeDatabase
import sa.sauditourism.employee.modules.account.domain.FiltersRepository
import sa.sauditourism.employee.modules.services.domain.RequestTypeRepository
import sa.sauditourism.employee.modules.account.myRequests.model.MyRequest
import sa.sauditourism.employee.modules.services.model.ServicesModel
import sa.sauditourism.employee.modules.services.model.ServicesResponseModel
import javax.inject.Inject

@HiltViewModel
class MyRequestsViewModel @Inject constructor(
    private val requestTypeRepository: RequestTypeRepository,
    private val filtersRepository: FiltersRepository,
    realTimeDatabase: RealTimeDatabase,
) : BaseViewModel(realTimeDatabase), DefaultLifecycleObserver {
    private val _myRequestsFlow =
        MutableStateFlow<UiState<List<MyRequest>>>(UiState.Loading())
    val myRequestsFlow: StateFlow<UiState<List<MyRequest>>> = _myRequestsFlow.asStateFlow()

    private var originalRequests: List<MyRequest> = emptyList()

    private val searchQuery = MutableStateFlow("")

    private val _servicesFiltersFlow =
        MutableStateFlow<UiState<List<ServicesModel>>>(UiState.Loading())
    val servicesFiltersFlow: StateFlow<UiState<List<ServicesModel>>> =
        _servicesFiltersFlow.asStateFlow()

    fun init() = launchIO {
        supervisorScope {
            asyncIO {
                getMyRequest()
                getFilters()
            }
        }
    }

    fun getMyRequest(requestTypeId: String? = null, requestOwnership: String? = null) = launchIO {
        requestTypeRepository.getMyRequests(requestTypeId, requestOwnership).collect {
            when (it) {
                is UiState.Success<*> -> {
                    it.data?.data?.let { response ->
                        originalRequests = response.requestsList
                        _myRequestsFlow.emit(UiState.Success(originalRequests))
                    }
                }

                is UiState.Error -> {
                    it.networkError?.let { error ->
                        _myRequestsFlow.emit(UiState.Error(error))
                    }
                }

                is UiState.Loading -> {
                    _myRequestsFlow.emit(UiState.Loading())
                }
            }

        }

    }

    fun getFilters() = launchIO {
        filtersRepository.getFilters().collect {
            when (it) {
                is UiState.Success<*> -> {
                    if (it.data?.code != 200) {
                        _servicesFiltersFlow.emit(UiState.Error(NetworkError(it.data?.message)))
                        return@collect
                    }
                    it.data.data?.let { response ->
                        _servicesFiltersFlow.emit(UiState.Success(response.servicesLList))
                    }
                }

                is UiState.Error -> {
                    it.networkError?.let { error ->
                        _myRequestsFlow.emit(UiState.Error(error))
                    }
                }

                is UiState.Loading -> {
                    _myRequestsFlow.emit(UiState.Loading())
                }
            }

        }
    }

    private fun observeSearchQuery() {
        searchQuery
            .debounce(300)
            .distinctUntilChanged()
            .onEach { query ->
                filter(query)
            }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChanged(query: String) {
        observeSearchQuery()
        searchQuery.value = query
    }

    fun filter(query: String) = launchIO {
        if (query.isEmpty()) {
            _myRequestsFlow.emit(UiState.Success(originalRequests))
        } else {
            val filteredRequests =
                originalRequests.filter { it.title.contains(query, ignoreCase = true) }
            _myRequestsFlow.emit(UiState.Success(filteredRequests))
        }

    }
}