package sa.sauditourism.employee.modules.services.details

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import sa.sauditourism.employee.base.BaseViewModel
import sa.sauditourism.employee.constants.CommonConstants.REQUEST_PARTICIPATED_DELETE_ACTION
import sa.sauditourism.employee.extensions.launchIO
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes.PARTICIPANTS_API_CODE
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes.PARTICIPANT_ACTION_API_CODE
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes.REQUEST_DETAILS_API_CODE
import sa.sauditourism.employee.managers.network.helpers.NetworkError
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.realtime.RealTimeDatabase
import sa.sauditourism.employee.modules.activities.model.RequestParticipantAction
import sa.sauditourism.employee.modules.services.domain.ParticipantsRepository
import sa.sauditourism.employee.modules.services.domain.RequestDetailsRepository
import sa.sauditourism.employee.modules.services.model.details.RequestDetailsModel
import sa.sauditourism.employee.modules.services.model.participants.Participant
import sa.sauditourism.employee.modules.services.model.participants.ParticipantsResponseModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RequestDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val requestDetailsRepository: RequestDetailsRepository,
    private val participantsRepository: ParticipantsRepository,
    realTimeDatabase: RealTimeDatabase
) : BaseViewModel(realTimeDatabase), DefaultLifecycleObserver {

    private val _detailsResultFlow =
        MutableStateFlow<UiState<RequestDetailsModel>>(UiState.Loading())
    val detailsResultFlow: StateFlow<UiState<RequestDetailsModel>> =
        _detailsResultFlow.asStateFlow()

    private val _participantsFlow =
        MutableStateFlow<UiState<ParticipantsResponseModel>>(UiState.Loading())
    val participantsFlow: StateFlow<UiState<ParticipantsResponseModel>> =
        _participantsFlow.asStateFlow()

    var requestTypeId: String = savedStateHandle.get<String>("id") ?: ""

    var participantsList = mutableStateListOf<Participant?>()

    fun getRequestDetails() = launchIO {
        requestDetailsRepository.getRequestDetails(requestTypeId).collect {
            when (it) {
                is UiState.Loading -> {
                    _detailsResultFlow.emit(UiState.Loading())
                    Timber.d("$REQUEST_DETAILS_API_CODE: UiState.Loading")
                }

                is UiState.Success<*> -> {
                    Timber.d("$REQUEST_DETAILS_API_CODE: UiState.Success")

                    it.data?.data?.let { response ->
                        _detailsResultFlow.emit(
                            if (it.data.code == 200) UiState.Success(response) else UiState.Error(
                                NetworkError(
                                    apiNumber = REQUEST_DETAILS_API_CODE,
                                    message = it.data.message
                                )
                            )
                        )
                    }
                }

                is UiState.Error -> {
                    it.networkError?.let { error ->
                        _detailsResultFlow.emit(UiState.Error(error))
                        Timber.d("$REQUEST_DETAILS_API_CODE: ${error.message}")
                    }
                }
            }
        }
    }

    fun getParticipates() = launchIO {
        participantsRepository.getParticipant(requestTypeId).collect {
            when (it) {
                is UiState.Loading -> {
                    _participantsFlow.emit(UiState.Loading())
                    Timber.d("$PARTICIPANTS_API_CODE: UiState.Loading")
                }

                is UiState.Success<*> -> {
                    it.data?.data?.let { response ->
                        _participantsFlow.emit(
                            if (it.data.code == 200) {
                                participantsList.clear()
                                participantsList.addAll(response.participants)
                                UiState.Success(response)
                            } else UiState.Error(
                                NetworkError(
                                    apiNumber = PARTICIPANTS_API_CODE,
                                    message = it.data.message
                                )
                            )
                        )
                    }
                    Timber.d("$PARTICIPANTS_API_CODE: UiState.Success")
                }

                is UiState.Error -> {
                    it.networkError?.let { error ->
                        _participantsFlow.emit(UiState.Error(error))
                        Timber.d("$PARTICIPANTS_API_CODE: ${error.message}")
                    }
                }
            }
        }
    }

    fun requestParticipantAction(
        ids: List<String>,
        action: String = REQUEST_PARTICIPATED_DELETE_ACTION
    ) = launchIO {

        val pairList = ArrayList<Pair<Int, Participant?>>().apply {
            clear()
            ids.onEach { id ->
                participantsList.run {
                    add(Pair(participantsList.indexOf(first { it?.name == id }), first { it?.name == id }))
                    removeIf { it?.name == id }
                }
            }
        }

        participantsRepository.requestParticipantAction(
            requestTypeId,
            RequestParticipantAction(ids, action)
        ).collect {
            when (it) {
                is UiState.Loading -> {
//                    _participantsFlow.emit(UiState.Loading())
                    Timber.d("$PARTICIPANT_ACTION_API_CODE: UiState.Loading")
                }

                is UiState.Success<*> -> {
                    it.data?.data?.let { response ->
                        _participantsFlow.emit(
                            if (it.data.code == 200) {
                                participantsList.clear()
                                participantsList.addAll(response.participants)
                                UiState.Success(response)
                            } else {
                                pairList.onEach { pair ->
                                    participantsList.add(pair.first , pair.second)
                                }
                                UiState.Error(
                                    NetworkError(
                                        apiNumber = PARTICIPANT_ACTION_API_CODE,
                                        message = it.data.message
                                    )
                                )
                            }
                        )
                    }
                    Timber.d("$PARTICIPANT_ACTION_API_CODE: UiState.Success")
                }

                is UiState.Error -> {
                    pairList.onEach { pair ->
                        participantsList.add(pair.first , pair.second)
                    }
                    it.networkError?.let { error ->
                        _participantsFlow.emit(UiState.Error(error))
                        Timber.d("$PARTICIPANT_ACTION_API_CODE: ${error.message}")
                    }
                }
            }
        }
    }

    override fun clearState()=launchIO {
        _participantsFlow.emit(UiState.Loading())
    }

}
