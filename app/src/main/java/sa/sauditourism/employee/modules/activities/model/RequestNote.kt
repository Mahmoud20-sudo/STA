package sa.sauditourism.employee.modules.activities.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RequestNote(
    @SerializedName("status") val status: String? = "",
    @SerializedName("date") val date: String? = "",
    @SerializedName("time") val time: String? = "",
): Parcelable
