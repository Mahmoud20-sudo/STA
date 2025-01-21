package sa.sauditourism.employee.modules.services.model


import com.google.gson.annotations.SerializedName

data class RadioButtonModel(
    @SerializedName("disabled")
    val disabled: Boolean,
    @SerializedName("id")
    val id: String,
    @SerializedName("self")
    val self: String,
    @SerializedName("value")
    val value: String
)