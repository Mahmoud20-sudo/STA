package sa.sauditourism.employee.modules.services.model.form.request


import com.google.gson.annotations.SerializedName

data class SubmitRequestBody(
    @SerializedName("value")
    val value: String? = null,
    @SerializedName("id")
    val id: String? = null
)