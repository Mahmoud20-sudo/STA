package sa.sauditourism.employee.modules.services.model


import com.google.gson.annotations.SerializedName

data class FormModel(
    @SerializedName("fields")
    val fields: List<Filed> = emptyList(),
    @SerializedName("formNote")
    var formNote: String? = ""
)