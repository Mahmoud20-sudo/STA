package sa.sauditourism.employee.modules.home.bookMeetingRoom.datasource

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import sa.sauditourism.employee.constants.ApiConstants
import sa.sauditourism.employee.constants.ApiConstants.ACT_EMAIL
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.modules.home.bookMeetingRoom.model.available_meeting_rooms.AvailableMeetingRoomsResponseModel

interface AvailableMeetingRoomsEndPoints {
    @GET(ApiConstants.CHECK_AVAILABLE_MEETING_ROOMS)
    suspend fun getAvailableRooms(
        @Query("locale") locale: String,
        @Query("date") date: String,
        @Query("time_from") timeFrom: String,
        @Query("time_to") timeTo: String,
        @Query(ACT_EMAIL) actEmail: String,
    ): Response<MainResponseModel<AvailableMeetingRoomsResponseModel>>

}