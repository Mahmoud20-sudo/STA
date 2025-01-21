package sa.sauditourism.employee.modules.services.model.form.request


import com.google.gson.annotations.SerializedName

data class FormSubmitRequestModel(
    @SerializedName("requestFieldValues")
    val requestFieldValues: Map<String, Any>,
    @SerializedName("requestTypeId")
    val requestTypeId: String,
    @SerializedName("serviceDeskId")
    val serviceDeskId: String
)