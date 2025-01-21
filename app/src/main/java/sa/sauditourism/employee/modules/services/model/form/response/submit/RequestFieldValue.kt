package sa.sauditourism.employee.modules.services.model.form.response.submit

import com.google.gson.annotations.SerializedName

data class RequestFieldValue(
    @SerializedName("fieldId")
    val fieldId: String,
    @SerializedName("label")
    val label: String,
    @SerializedName("renderedValue")
    val renderedValue: Any,
    @SerializedName("value")
    val value: Any
)