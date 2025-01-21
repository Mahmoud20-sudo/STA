package sa.sauditourism.employee.modules.services.datasource.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import sa.sauditourism.employee.constants.ApiConstants
import sa.sauditourism.employee.constants.ApiConstants.REQUEST_ID
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.modules.services.model.details.RequestDetailsModel

interface RequestDetailsEndpoints {
    @GET(ApiConstants.REQUEST_DETAILS)
    suspend fun getRequestDetails(
        @Path(REQUEST_ID) id: String,
        @Query("locale") locale: String
    ): Response<MainResponseModel<RequestDetailsModel>>
}
