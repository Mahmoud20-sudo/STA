package sa.sauditourism.employee.modules.services.model.details


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Status(
    @SerializedName("date")
    val date: String? = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("time")
    val time: String? = ""
) : Parcelable