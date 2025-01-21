package sa.sauditourism.employee.modules.login.domain

import com.google.gson.annotations.SerializedName

data class ProfileModel(
    @SerializedName("name")
    val name:String
)