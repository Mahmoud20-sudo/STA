package sa.sauditourism.employee.modules.account.myRequests.model

import com.google.gson.annotations.SerializedName

data class MyRequest(
    @SerializedName("id")
    val id:String="",
    @SerializedName("title")
    val title:String="",
    @SerializedName("icon")
    val icon:String="",
    @SerializedName("currentStatus")
    val currentStatus:String="",
    @SerializedName("date")
    val date:String="",
    @SerializedName("time")
    val time:String=""
)