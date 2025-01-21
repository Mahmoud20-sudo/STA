package sa.sauditourism.employee.modules.services.model.form.response.submit


import com.google.gson.annotations.SerializedName

data class LinksX(
    @SerializedName("avatarUrls")
    val avatarUrls: AvatarUrls,
    @SerializedName("jiraRest")
    val jiraRest: String,
    @SerializedName("self")
    val self: String
)