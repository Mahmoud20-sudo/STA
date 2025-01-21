package sa.sauditourism.employee.modules.services.model.form.request


import com.google.gson.annotations.SerializedName

data class RequestFieldValues(
    @SerializedName("attachment")
    val attachment: List<String>,
    @SerializedName("customfield_10105")
    val customField10105: CustomField10105,
    @SerializedName("customfield_10111")
    val customField10111: CustomField10105,
    @SerializedName("customfield_18003")
    val customField18003: CustomField18003,
    @SerializedName("description")
    val description: String
)