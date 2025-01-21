package sa.sauditourism.employee.modules.services.domain

import kotlinx.coroutines.flow.Flow
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.helpers.ApiResult
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.network.networkBoundNoCacheResource
import sa.sauditourism.employee.modules.activities.model.RequestParticipantAction
import sa.sauditourism.employee.modules.services.datasource.remote.ParticipantsEndpoints
import sa.sauditourism.employee.modules.services.model.participants.ParticipantsResponseModel
import javax.inject.Inject

class ParticipantsRepositoryImpl @Inject constructor(
    private val endPoints: ParticipantsEndpoints
) : ParticipantsRepository {
    override fun getParticipant(id: String): Flow<UiState<MainResponseModel<ParticipantsResponseModel>>> = networkBoundNoCacheResource(
        fetch = {
            ApiResult.create(
                endPoints.getParticipants(
                    id = id,
                    locale = EmployeeApplication.sharedPreferencesManager.preferredLocale
                        ?: LanguageConstants.DEFAULT_LOCALE,
                ),
                ApiNumberCodes.REQUEST_DETAILS_API_CODE,
            )
        },
    )

    override fun requestParticipantAction(id: String, action: RequestParticipantAction): Flow<UiState<MainResponseModel<ParticipantsResponseModel>>> = networkBoundNoCacheResource(
        fetch = {
            ApiResult.create(
                endPoints.requestParticipantAction(
                    id= id,
                    action = action,
                    locale = EmployeeApplication.sharedPreferencesManager.preferredLocale
                        ?: LanguageConstants.DEFAULT_LOCALE,
                ),
                ApiNumberCodes.REQUEST_DETAILS_API_CODE,
            )
        },
    )
}