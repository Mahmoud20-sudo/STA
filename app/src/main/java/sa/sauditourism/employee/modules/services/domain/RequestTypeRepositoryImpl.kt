package sa.sauditourism.employee.modules.services.domain

import kotlinx.coroutines.flow.Flow
import sa.sauditourism.employee.EmployeeApplication
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.managers.network.helpers.ApiNumberCodes
import sa.sauditourism.employee.managers.network.helpers.ApiResult
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.managers.network.networkBoundNoCacheResource
import sa.sauditourism.employee.managers.sharedprefrences.SharedPreferencesManager
import sa.sauditourism.employee.modules.services.datasource.remote.RequestTypeEndPoints
import sa.sauditourism.employee.modules.services.model.RequestTypeResponse
import sa.sauditourism.employee.modules.account.myRequests.model.MyRequestsResponse

class RequestTypeRepositoryImpl(
    private val endPoints: RequestTypeEndPoints,
    private val sharedPreferencesManager: SharedPreferencesManager
) : RequestTypeRepository {

    override fun searchAllRequestTypes(query: String): Flow<UiState<MainResponseModel<RequestTypeResponse>>> =
        networkBoundNoCacheResource(
            fetch = {
                ApiResult.create(
                    endPoints.search(
                        query = query,
                        locale = sharedPreferencesManager.preferredLocale
                            ?: LanguageConstants.DEFAULT_LOCALE,
                    ),
                    ApiNumberCodes.REQUESTS_TYPES_API_CODE,
                )
            },
        )

    override fun searchRequestTypesById(
        id: String,
        query: String
    ): Flow<UiState<MainResponseModel<RequestTypeResponse>>> = networkBoundNoCacheResource(
        fetch = {
            ApiResult.create(
                endPoints.searchWithServiceId(
                    serviceId = id,
                    query = query,
                    locale = sharedPreferencesManager.preferredLocale
                        ?: LanguageConstants.DEFAULT_LOCALE
                ),
                ApiNumberCodes.REQUESTS_TYPES_API_CODE,
            )
        },
    )

    override fun getMyRequests(
        requestTypeId: String?,
        requestOwnership: String?
    ): Flow<UiState<MainResponseModel<MyRequestsResponse>>> = networkBoundNoCacheResource(
        fetch = {
            ApiResult.create(
                endPoints.getMyRequests(
                    locale = sharedPreferencesManager.preferredLocale
                        ?: LanguageConstants.DEFAULT_LOCALE,
                    requestTypeId = requestTypeId,
                    requestOwnership = requestOwnership
                ),
                ApiNumberCodes.MY_REQUESTS_API_CODE,
            )
        }
    )
}