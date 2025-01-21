package sa.sauditourism.employee.modules.services.model.form.response.dropDown


import com.google.gson.annotations.SerializedName

data class AvatarUrls(
    @SerializedName("16x16")
    val x16: String,
    @SerializedName("24x24")
    val x24: String,
    @SerializedName("32x32")
    val x32: String,
    @SerializedName("48x48")
    val x48: String
)