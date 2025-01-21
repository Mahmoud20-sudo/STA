package sa.sauditourism.employee.modules.services.model.form.response.dropDown


import com.google.gson.annotations.SerializedName

data class CustomerRequest(
    @SerializedName("createdDate")
    val createdDate: CreatedDate,
    @SerializedName("currentStatus")
    val currentStatus: CurrentStatus,
    @SerializedName("_expands")
    val expands: List<String>,
    @SerializedName("issueId")
    val issueId: String,
    @SerializedName("issueKey")
    val issueKey: String,
    @SerializedName("_links")
    val links: Links,
    @SerializedName("reporter")
    val reporter: Reporter,
    @SerializedName("requestFieldValues")
    val requestFieldValues: List<RequestFieldValue>,
    @SerializedName("requestTypeId")
    val requestTypeId: String,
    @SerializedName("serviceDeskId")
    val serviceDeskId: String
)