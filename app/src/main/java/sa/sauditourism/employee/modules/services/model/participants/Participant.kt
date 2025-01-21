package sa.sauditourism.employee.modules.services.model.participants


import com.google.gson.annotations.SerializedName

data class Participant(
    @SerializedName("displayName")
    val displayName: String = "",
    @SerializedName("emailAdress")
    val emailAdress: String = "",
    @SerializedName("key")
    val key: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("userImage")
    val userImage: String = ""
)