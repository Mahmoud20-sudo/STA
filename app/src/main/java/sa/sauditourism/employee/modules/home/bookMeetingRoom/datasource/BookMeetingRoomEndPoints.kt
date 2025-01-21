package sa.sauditourism.employee.modules.home.bookMeetingRoom.datasource

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import sa.sauditourism.employee.constants.ApiConstants
import sa.sauditourism.employee.constants.ApiConstants.ACT_EMAIL
import sa.sauditourism.employee.constants.ApiConstants.MEETING_ROOM_ID
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.modules.home.bookMeetingRoom.model.single_meeting_room.request.BookMeetingRoomRequestModel
import sa.sauditourism.employee.modules.home.bookMeetingRoom.model.single_meeting_room.response.BookMeetingRoomResponse
import sa.sauditourism.employee.modules.services.model.form.request.FormSubmitRequestModel
import sa.sauditourism.employee.modules.services.model.form.response.submit.FormSubmitResponseModel

interface BookMeetingRoomEndPoints {

    @POST(ApiConstants.BOOK_MEETING_ROOM)
    suspend fun bookRoom(
        @Path(MEETING_ROOM_ID) meetingRoomId: String,
        @Query("locale") locale: String,
        @Query(ACT_EMAIL) actEmail: String,
        @Body bookMeetingRoomRequestModel: BookMeetingRoomRequestModel
    ): Response<MainResponseModel<BookMeetingRoomResponse>>
}