package sa.sauditourism.employee.modules.home.bookMeetingRoom.model.available_meeting_rooms


import com.google.gson.annotations.SerializedName

data class AvailableMeetingRoomsResponseModel(
    @SerializedName("meetingRooms")
    val meetingRooms: List<MeetingRoom>
)