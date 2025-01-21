package sa.sauditourism.employee.modules.home.bookMeetingRoom.model.single_meeting_room.response


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class BookMeetingRoomResponse(
    @SerialName("message")
    val message: String? = null,
    @SerializedName("errors")
    val errors: List<Error>? = emptyList()
)

data class Error(
    val errorMessage: String?
)