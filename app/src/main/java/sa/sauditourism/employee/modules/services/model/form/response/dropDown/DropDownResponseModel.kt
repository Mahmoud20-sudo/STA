package sa.sauditourism.employee.modules.services.model.form.response.dropDown


import com.google.gson.annotations.SerializedName

data class DropDownResponseModel(
    @SerializedName("users")
    val fieldUserResponseModel: List<FieldUserResponseModel>,
    @SerializedName("dropDownValues")
    val fieldResponseModel: List<FieldResponseModel>
)