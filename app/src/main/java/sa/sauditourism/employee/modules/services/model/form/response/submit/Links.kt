package sa.sauditourism.employee.modules.services.model.form.response.submit


import com.google.gson.annotations.SerializedName

data class Links(
    @SerializedName("self")
    val self: String,
    @SerializedName("web")
    val web: String
)