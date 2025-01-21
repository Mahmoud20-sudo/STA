package sa.sauditourism.employee.modules.services.model.form.response


import com.google.gson.annotations.SerializedName
import sa.sauditourism.employee.modules.services.model.FormModel

data class FormResponseModel(
    @SerializedName("form")
    val form: FormModel?
)