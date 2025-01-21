package sa.sauditourism.employee.modules.services.domain

import kotlinx.coroutines.flow.Flow
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.modules.services.model.details.RequestDetailsModel

interface RequestDetailsRepository {
    fun getRequestDetails(id: String): Flow<UiState<MainResponseModel<RequestDetailsModel>>>
}