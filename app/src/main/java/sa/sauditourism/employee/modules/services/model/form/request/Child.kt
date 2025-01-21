package sa.sauditourism.employee.modules.services.model.form.request


import com.google.gson.annotations.SerializedName

data class Child(
    @SerializedName("id")
    val id: String,
    @SerializedName("value")
    val value: String
)