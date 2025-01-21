package sa.sauditourism.employee.modules.services.model.form.request


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import sa.sauditourism.employee.components.DropDownModel

@Serializable
data class RequestFieldValue(
    @SerializedName("fieldId")
    val fieldId: String,
    @SerializedName("label")
    val label: String,
    @SerializedName("renderedValue")
    val renderedValue: List<String>,
    @SerializedName("value")
    val value: String
){
    object ModelMapper {
        fun from(form: RequestFieldValue) =
            DropDownModel(stringId = form.fieldId, label = form.value)
    }
}