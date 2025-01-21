package sa.sauditourism.employee.modules.account.datasource

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import sa.sauditourism.employee.constants.ApiConstants
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.modules.services.model.ServicesResponseModel

interface FiltersEndPoints {
    @GET(ApiConstants.SERVICES_FILTERS)
    suspend fun getFilters(
        @Query("locale") locale: String,
    ): Response<MainResponseModel<ServicesResponseModel>>
}