package sa.sauditourism.employee.modules.services.model.details


import com.google.gson.annotations.SerializedName

data class RequestDetailsModel(
    @SerializedName("request")
    val request: Request
)