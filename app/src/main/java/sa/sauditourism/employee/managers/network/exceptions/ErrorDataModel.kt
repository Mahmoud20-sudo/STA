package sa.sauditourism.employee.managers.network.exceptions

import com.google.gson.annotations.SerializedName

data class ErrorDataModel(
    @SerializedName("status") val status: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("statusCode") val errorCode: String?,
)