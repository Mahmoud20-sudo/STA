package sa.sauditourism.employee.modules.home.model


import com.google.gson.annotations.SerializedName

data class Action(
    @SerializedName("commingSoon")
    val commingSoon: Boolean = false,
    @SerializedName("deepLink")
    val deepLink: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("orderIndex")
    val orderIndex: Int = 0,
    @SerializedName("picToGram")
    val picToGram: String = "",
    @SerializedName("requestType")
    val requestType: String = "",
    @SerializedName("serviceCategoryId")
    val serviceCategoryId: String = "",
    @SerializedName("serviceId")
    val serviceId: String = "",
    @SerializedName("title")
    val title: String = ""
)