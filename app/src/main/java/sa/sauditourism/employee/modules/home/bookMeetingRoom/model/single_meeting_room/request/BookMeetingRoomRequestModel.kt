package sa.sauditourism.employee.modules.home.bookMeetingRoom.model.single_meeting_room.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookMeetingRoomRequestModel(
    @SerialName("attendees")
    val attendees: List<String?>? = null,
    @SerialName("body")
    val body: String? = null,
    @SerialName("date")
    val date: String? = null,
    @SerialName("end")
    val end: String? = null,
    @SerialName("start")
    val start: String? = null,
    @SerialName("title")
    val title: String? = null
)