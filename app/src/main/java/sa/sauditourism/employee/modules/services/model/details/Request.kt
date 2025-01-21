package sa.sauditourism.employee.modules.services.model.details


import com.google.gson.annotations.SerializedName

data class Request(
    @SerializedName("addAttachment")
    val addAttachment: Boolean = false,
    @SerializedName("addComment")
    val addComment: Boolean = false,
    @SerializedName("addParticipant")
    val addParticipant: Boolean = false,
    @SerializedName("currentStatus")
    val currentStatus: String = "",
    @SerializedName("date")
    val date: String = "",
    @SerializedName("fields")
    val fields: List<Field> = emptyList(),
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("requestTypeId")
    val requestTypeId: String? = null,
    @SerializedName("issueId")
    val issueId: String? = null,
    @SerializedName("participants")
    val participants: List<Any> = emptyList(),
    @SerializedName("removeParticipant")
    val removeParticipant: Boolean = false,
    @SerializedName("serviceCategoryTitle")
    val serviceCategoryTitle: String = "",
    @SerializedName("status")
    val status: List<Status> = emptyList(),
    @SerializedName("time")
    val time: String = "",
    @SerializedName("title")
    val title: String = "",
)

