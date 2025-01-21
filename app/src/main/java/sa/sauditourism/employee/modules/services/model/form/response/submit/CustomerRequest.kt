package sa.sauditourism.employee.modules.services.model.form.response.submit


import com.google.gson.annotations.SerializedName

data class CustomerRequest(
    @SerializedName("createdDate")
    val createdDate: CreatedDate? = null,
    @SerializedName("currentStatus")
    val currentStatus: CurrentStatus? = null,
    @SerializedName("_expands")
    val expands: List<String>? = null,
    @SerializedName("issueId")
    val issueId: String? = null,
    @SerializedName("issueKey")
    val issueKey: String? = null,
    @SerializedName("_links")
    val links: Links? = null,
    @SerializedName("reporter")
    val reporter: Reporter? = null,
    @SerializedName("requestFieldValues")
    val requestFieldValues: List<RequestFieldValue>? = null,
    @SerializedName("requestTypeId")
    val requestTypeId: String? = null,
    @SerializedName("serviceDeskId")
    val serviceDeskId: String? = null
)