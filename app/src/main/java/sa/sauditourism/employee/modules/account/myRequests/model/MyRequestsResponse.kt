package sa.sauditourism.employee.modules.account.myRequests.model

import com.google.gson.annotations.SerializedName

data class MyRequestsResponse(
    @SerializedName("requests")
    val requestsList: List<MyRequest>
)
