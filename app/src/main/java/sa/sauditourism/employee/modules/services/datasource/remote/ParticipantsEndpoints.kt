package sa.sauditourism.employee.modules.services.datasource.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import sa.sauditourism.employee.constants.ApiConstants
import sa.sauditourism.employee.constants.ApiConstants.REQUEST_ID
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.modules.activities.model.RequestParticipantAction
import sa.sauditourism.employee.modules.services.model.participants.ParticipantsResponseModel

interface ParticipantsEndpoints {

    @GET(ApiConstants.PARTICIPANTS_FILTERS)
    suspend fun getParticipants(
        @Path(REQUEST_ID) id: String,
        @Query("locale") locale: String,
    ): Response<MainResponseModel<ParticipantsResponseModel>>

    @POST(ApiConstants.PARTICIPANT_ACTION)
    suspend fun requestParticipantAction(
        @Path(REQUEST_ID) id: String,
        @Body action: RequestParticipantAction,
        @Query("locale") locale: String
    ): Response<MainResponseModel<ParticipantsResponseModel>>
}
