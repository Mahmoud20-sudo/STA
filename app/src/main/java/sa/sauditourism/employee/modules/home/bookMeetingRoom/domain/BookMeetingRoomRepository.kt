package sa.sauditourism.employee.modules.home.bookMeetingRoom.domain

import kotlinx.coroutines.flow.Flow
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.modules.home.bookMeetingRoom.model.single_meeting_room.request.BookMeetingRoomRequestModel
import sa.sauditourism.employee.modules.home.bookMeetingRoom.model.single_meeting_room.response.BookMeetingRoomResponse
import sa.sauditourism.employee.modules.services.model.form.request.FormSubmitRequestModel
import sa.sauditourism.employee.modules.services.model.form.response.submit.FormSubmitResponseModel

interface BookMeetingRoomRepository {

    fun bookMeeting(
        meetingId: String,
        bookMeetRequestModel: BookMeetingRoomRequestModel
    ): Flow<UiState<MainResponseModel<BookMeetingRoomResponse>>>//

}