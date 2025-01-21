package sa.sauditourism.employee.modules.services.datasource.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import sa.sauditourism.employee.constants.ApiConstants.ACT_EMAIL
import sa.sauditourism.employee.constants.ApiConstants.MY_REQUESTS
import sa.sauditourism.employee.constants.ApiConstants.REQUESTS_TYPES
import sa.sauditourism.employee.constants.ApiConstants.REQUESTS_TYPES_SEARCH
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.modules.services.model.RequestTypeResponse
import sa.sauditourism.employee.modules.account.myRequests.model.MyRequestsResponse

interface RequestTypeEndPoints {
    @GET(REQUESTS_TYPES_SEARCH)
    suspend fun search(
        @Query("query") query: String,
        @Query("locale") locale: String,
    ): Response<MainResponseModel<RequestTypeResponse>>

    @GET(REQUESTS_TYPES)
    suspend fun searchWithServiceId(
        @Path("serviceId") serviceId: String,
        @Query("query") query: String,
        @Query("locale") locale: String
    ): Response<MainResponseModel<RequestTypeResponse>>

    @GET(MY_REQUESTS)
    suspend fun getMyRequests(
        @Query("locale") locale: String,
        @Query("requestTypeId") requestTypeId: String? = null,
        @Query("request_ownership") requestOwnership: String? = null
    ): Response<MainResponseModel<MyRequestsResponse>>
}