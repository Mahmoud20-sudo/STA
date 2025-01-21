package sa.sauditourism.employee.modules.services.model.form.response.dropDown


import com.google.gson.annotations.SerializedName
import sa.sauditourism.employee.components.DropDownModel

data class FieldResponseModel(
    @SerializedName("label")
    val label: String,
    @SerializedName("children")
    val children: List<FieldResponseModel> = emptyList(),
    @SerializedName("value")
    val value: String
) {
    object ModelMapper {
        fun from(form: FieldResponseModel): DropDownModel {
            //mapping children in case of MULTI-DROPDOWN
            val innerList = arrayListOf<DropDownModel>()
            if (form.children.isNotEmpty())
                form.children.onEach {
                    innerList.add(from(it))
                }
            return DropDownModel(stringId = form.value, label = form.label, innerList = innerList)
        }
    }
}