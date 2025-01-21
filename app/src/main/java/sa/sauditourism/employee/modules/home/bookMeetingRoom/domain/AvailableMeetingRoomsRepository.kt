package sa.sauditourism.employee.modules.home.bookMeetingRoom.domain

import kotlinx.coroutines.flow.Flow
import sa.sauditourism.employee.managers.network.models.MainResponseModel
import sa.sauditourism.employee.managers.network.models.UiState
import sa.sauditourism.employee.modules.home.bookMeetingRoom.model.available_meeting_rooms.AvailableMeetingRoomsResponseModel

interface AvailableMeetingRoomsRepository {
    fun getAvailableMeetingRooms(
        date:String,
        timeFrom:String,
        timeTo:String
    ): Flow<UiState<MainResponseModel<AvailableMeetingRoomsResponseModel>>>

}