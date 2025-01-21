package sa.sauditourism.employee.modules.services.model.form.response.dropDown


import com.google.gson.annotations.SerializedName

data class StatusDate(
    @SerializedName("epochMillis")
    val epochMillis: Long,
    @SerializedName("friendly")
    val friendly: String,
    @SerializedName("iso8601")
    val iso8601: String,
    @SerializedName("jira")
    val jira: String
)