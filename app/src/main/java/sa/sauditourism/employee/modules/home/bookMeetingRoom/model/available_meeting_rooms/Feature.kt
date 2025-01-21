package sa.sauditourism.employee.modules.home.bookMeetingRoom.model.available_meeting_rooms


import com.google.gson.annotations.SerializedName

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Feature(
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("name")
    val name: String = ""
) : Parcelable