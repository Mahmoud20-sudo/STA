package sa.sauditourism.employee.modules.activities.participants.addParticipant
import androidx.lifecycle.DefaultLifecycleObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import sa.sauditourism.employee.base.BaseViewModel
import sa.sauditourism.employee.components.DropDownModel
import sa.sauditourism.employee.extensions.launchIO
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes.ADD_USER__API_CODE
import sa.sauditourism.employee.managers.network.helpers.NetworkError
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.realtime.RealTimeDatabase
import sa.sauditourism.employee.modules.activities.domain.ActivitiesRepository
import sa.sauditourism.employee.modules.activities.model.AddUserResponse
import sa.sauditourism.employee.modules.activities.model.SearchUserResponse
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddParticipantViewModel @Inject constructor(
    private val activitiesRepository: ActivitiesRepository,
    realTimeDatabase: RealTimeDatabase
) : BaseViewModel(realTimeDatabase), DefaultLifecycleObserver {

    private val _usersFlow =
        MutableStateFlow<UiState<SearchUserResponse>>(UiState.Loading())
    val usersFlow: StateFlow<UiState<SearchUserResponse>> = _usersFlow.asStateFlow()

    private val _userAddedFlow =
        MutableStateFlow<UiState<AddUserResponse>>(UiState.Loading())
    val addedFlow: StateFlow<UiState<AddUserResponse>> = _userAddedFlow.asStateFlow()

    private val _showLoading =
        MutableStateFlow<Boolean>(false)
    val showLoading: StateFlow<Boolean> = _showLoading.asStateFlow()


    fun searchForUser(
        query: String,
        // if the flag is true then get the available participant for a meeting rooms
        locally: Boolean? = false
    ) = launchIO {
        activitiesRepository.searchForUser(query, locally).collect {
            when (it) {
                is UiState.Loading -> {
                    Timber.d("${ApiNumberCodes.SEARCH__API_CODE}: UiState.Loading")
                    _usersFlow.emit(UiState.Loading())
                }

                is UiState.Success<*> -> {
                    Timber.d("${ApiNumberCodes.SEARCH__API_CODE}: UiState.Success")
                    it.data?.data?.let { response ->

                        _usersFlow.emit(UiState.Success(response))
                    }
                }

                is UiState.Error -> {
                    it.networkError?.let { error ->
                        Timber.d("${ApiNumberCodes.SEARCH__API_CODE}: ${error.message}")
                        _usersFlow.emit(UiState.Error(NetworkError()))
                    }
                }

            }
        }
    }

    fun addUsers(requestId: String, userList: MutableList<DropDownModel>) = launchIO {
        activitiesRepository.addUsers(requestId, getListOfUser(userList)).collect {
            when (it) {
                is UiState.Loading -> {
                    _showLoading.emit(true)
                    _userAddedFlow.emit(UiState.Loading())
                }

                is UiState.Success<*> -> {
                    _showLoading.emit(false)
                    Timber.d("${ADD_USER__API_CODE}: UiState.Success")
                    if (it.data?.code == 200) {
                        it.data.data?.let { response ->
                            _userAddedFlow.emit(UiState.Success(response))
                        }
                    } else {
                        _userAddedFlow.emit(
                            UiState.Error(
                                NetworkError(
                                    apiNumber = ADD_USER__API_CODE,
                                    message = it.data?.message
                                )
                            )
                        )
                    }

                }

                is UiState.Error -> {
                    _showLoading.emit(false)
                    it.networkError?.let { error ->
                        Timber.d("${ADD_USER__API_CODE}: ${error.message}")
                        _userAddedFlow.emit(UiState.Error(NetworkError()))
                    }
                }

            }
        }
    }

    private fun getListOfUser(dropDownsModelSelected: MutableList<DropDownModel>): List<String> {
        var userNameList: MutableList<String> = mutableListOf()

        dropDownsModelSelected.forEach { dropItem ->
            val item = usersFlow.value.data?.users?.find { it.displayName == dropItem.label }
            item?.let {
                userNameList.add(it.name)
            }
        }
        return userNameList

    }

    fun clearUsers() = launchIO {
        _usersFlow.emit(UiState.Loading())
    }

    override fun clearState() = launchIO {
        _usersFlow.emit(UiState.Loading())
        _userAddedFlow.emit(UiState.Loading())
    }

}