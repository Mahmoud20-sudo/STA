package sa.sauditourism.employee.modules.home.datasource.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import sa.sauditourism.employee.constants.ApiConstants
import sa.sauditourism.employee.constants.ApiConstants.ACT_EMAIL
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.modules.home.model.AnnouncementResponse
import sa.sauditourism.employee.modules.home.model.QuickActionsResponseModel

interface HomeEndpoints {

    @GET(ApiConstants.GET_ANNOUNCEMENT)
    suspend fun getAnnouncement(
        @Query("locale") locale: String,
        @Query(ACT_EMAIL) actEmail: String
    ): Response<MainResponseModel<AnnouncementResponse>>

    @GET(ApiConstants.MY_DAY_ACTIONS)
    suspend fun getQuickActions(
        @Query("locale") locale: String,
    ): Response<MainResponseModel<QuickActionsResponseModel>>
}