package sa.sauditourism.employee.modules.services.model.form.response.dropDown


import com.google.gson.annotations.SerializedName
import sa.sauditourism.employee.components.DropDownModel

data class FieldUserResponseModel(
    @SerializedName("key")
    val key: String?,
    @SerializedName("displayName")
    val displayName: String,
    @SerializedName("name")
    val name: String
){
    object ModelMapper {
        fun from(form: FieldUserResponseModel) =
            DropDownModel(stringId = form.key, label = form.displayName, name = form.name)
    }
}