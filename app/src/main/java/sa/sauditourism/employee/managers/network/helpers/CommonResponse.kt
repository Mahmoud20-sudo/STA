package sa.sauditourism.employee.managers.network.helpers

import com.google.gson.annotations.SerializedName

data class CommonResponse<T>(
    @SerializedName("error") val error: Boolean = false,
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String? = null,
    @SerializedName("response") val response: T?
)
