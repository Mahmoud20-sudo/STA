package sa.sauditourism.employee.modules.activities.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RequestParticipantAction(
    @SerializedName("usernames") val usernames: List<String>? = null,
    @SerializedName("action") val action: String? = "",
): Parcelable
