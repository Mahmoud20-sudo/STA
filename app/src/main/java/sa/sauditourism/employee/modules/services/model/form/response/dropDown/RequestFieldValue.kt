package sa.sauditourism.employee.modules.services.model.form.response.dropDown


import com.google.gson.annotations.SerializedName

data class RequestFieldValue(
    @SerializedName("fieldId")
    val fieldId: String,
    @SerializedName("label")
    val label: String,
    @SerializedName("renderedValue")
    val renderedValue: List<Any>,
    @SerializedName("value")
    val value: String
)