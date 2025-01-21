package sa.sauditourism.employee.modules.activities.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import sa.sauditourism.employee.modules.services.model.form.response.Attachment

@Serializable
data class RequestCommentResponse(
    @SerializedName("comments")
    val comments: List<RequestComment> = emptyList(),
)

@Serializable
data class RequestComment(
    @SerializedName("id") val id: String? = "",
    @SerializedName("body") var body: String? = "",
    @SerializedName("user") val user: RequestCommentUser? = RequestCommentUser(),
    @SerializedName("date") val date: String? = "",
    @SerializedName("time") val time: String? = "",
    var attachments: MutableList<Attachment> = mutableListOf(),
)
@Serializable
data class RequestCommentUser(
    @SerializedName("userImage") val userImage: String? = "",
    @SerializedName("name") val name: String? = "",
    @SerializedName("emailAdress") val emailAdress: String? = "",
    @SerializedName("displayName") val displayName: String? = "",
)
