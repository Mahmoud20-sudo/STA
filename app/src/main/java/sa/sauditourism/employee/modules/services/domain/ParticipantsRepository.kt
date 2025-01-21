package sa.sauditourism.employee.modules.services.domain

import kotlinx.coroutines.flow.Flow

import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.modules.activities.model.RequestParticipantAction
import sa.sauditourism.employee.modules.services.model.participants.ParticipantsResponseModel

interface ParticipantsRepository {
    fun getParticipant(id: String): Flow<UiState<MainResponseModel<ParticipantsResponseModel>>>
    fun requestParticipantAction(id: String, action: RequestParticipantAction): Flow<UiState<MainResponseModel<ParticipantsResponseModel>>>
}