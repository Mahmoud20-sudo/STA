package sa.sauditourism.employee.modules.account.model


import com.google.gson.annotations.SerializedName

data class DeleteProfileResponseModel(
    @SerializedName("profilePicture")
    val profilePicture: String? = null
)