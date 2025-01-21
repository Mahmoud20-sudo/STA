package sa.sauditourism.employee.modules.home.bookMeetingRoom.model.available_meeting_rooms


import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Room(
    @SerializedName("capacity")
    val capacity: Int = 0,
    @SerializedName("email")
    val email: String = "",
    @SerializedName("features")
    val features: List<Feature> = emptyList(),
    @SerializedName("floor")
    val floor: String = "",
    @SerializedName("id")
    val id: String = "",
    @SerializedName("image")
    val image: String? = "",
    @SerializedName("title")
    val title: String = ""
) : Parcelable