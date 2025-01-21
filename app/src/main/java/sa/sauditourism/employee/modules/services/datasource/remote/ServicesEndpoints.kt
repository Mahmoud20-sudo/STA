package sa.sauditourism.employee.modules.services.datasource.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import sa.sauditourism.employee.constants.ApiConstants
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.modules.services.model.ServicesResponseModel

interface ServicesEndpoints {

    @GET(ApiConstants.GET_SERVICES)
    suspend fun getServices(
        @Query("locale") locale: String,//todo add to interceptor
    ): Response<MainResponseModel<ServicesResponseModel>>



}
