package sa.sauditourism.employee.modules.home.myDayMeetings.datasource.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import sa.sauditourism.employee.constants.ApiConstants
import sa.sauditourism.employee.constants.ApiConstants.ACT_EMAIL
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.modules.home.myDayMeetings.model.MyDayMeetingsResponseModel

interface MyMeetingsEndPoints {

    @GET(ApiConstants.MY_DAY_MEETINGS)
    suspend fun getMyDayMeetings(
        @Query("locale") locale: String,
        @Query(ACT_EMAIL) actEmail: String,
    ): Response<MainResponseModel<MyDayMeetingsResponseModel>>

}