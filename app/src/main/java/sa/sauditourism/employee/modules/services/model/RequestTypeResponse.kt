package sa.sauditourism.employee.modules.services.model

import com.google.gson.annotations.SerializedName

data class RequestTypeResponse(
    @SerializedName("requestsTypes")
    val requestsTypesList: List<RequestsType>
)
