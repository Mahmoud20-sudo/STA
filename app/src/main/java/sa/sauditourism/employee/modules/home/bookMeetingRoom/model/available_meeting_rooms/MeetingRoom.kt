package sa.sauditourism.employee.modules.home.bookMeetingRoom.model.available_meeting_rooms


import com.google.gson.annotations.SerializedName

data class MeetingRoom(
    @SerializedName("floor")
    val floor: String = "",
    @SerializedName("rooms")
    val rooms: List<Room> = emptyList()
)