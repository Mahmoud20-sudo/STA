package sa.sauditourism.employee.modules.services.domain

import kotlinx.coroutines.flow.Flow
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.modules.services.model.RequestTypeResponse
import sa.sauditourism.employee.modules.account.myRequests.model.MyRequestsResponse

interface RequestTypeRepository {
     fun searchAllRequestTypes(query: String): Flow<UiState<MainResponseModel<RequestTypeResponse>>>
     fun searchRequestTypesById(id: String, query: String): Flow<UiState<MainResponseModel<RequestTypeResponse>>>
     fun getMyRequests(requestTypeId : String?, requestOwnership: String?): Flow<UiState<MainResponseModel<MyRequestsResponse>>>
}