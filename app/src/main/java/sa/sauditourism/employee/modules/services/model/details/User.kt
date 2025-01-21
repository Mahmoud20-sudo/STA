package sa.sauditourism.employee.modules.services.model.details


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerializedName("displayName")
    val displayName: String,
    @SerializedName("emailAdress")
    val emailAdress: String,
    @SerializedName("key")
    val key: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("userImage")
    val userImage: String
)