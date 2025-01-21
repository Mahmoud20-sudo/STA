package sa.sauditourism.employee.modules.services.model.form.response.dropDown


import com.google.gson.annotations.SerializedName

data class Reporter(
    @SerializedName("active")
    val active: Boolean,
    @SerializedName("displayName")
    val displayName: String,
    @SerializedName("emailAddress")
    val emailAddress: String,
    @SerializedName("key")
    val key: String,
    @SerializedName("_links")
    val links: LinksX,
    @SerializedName("name")
    val name: String,
    @SerializedName("timeZone")
    val timeZone: String
)