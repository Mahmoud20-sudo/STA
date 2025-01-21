package sa.sauditourism.employee.modules.services.model.form.response.submit


import com.google.gson.annotations.SerializedName

data class CurrentStatus(
    @SerializedName("status")
    val status: String,
    @SerializedName("statusDate")
    val statusDate: StatusDate
)