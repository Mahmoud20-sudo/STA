package sa.sauditourism.employee.models.ssid

import com.google.gson.annotations.SerializedName

data class LogoutModel(
    @SerializedName("data")
    val `data`: Any,
    @SerializedName("status")
    val status: String,
    @SerializedName("faultString")
    val faultString: Any,
    @SerializedName("faultCode")
    val faultCode: Int
)
