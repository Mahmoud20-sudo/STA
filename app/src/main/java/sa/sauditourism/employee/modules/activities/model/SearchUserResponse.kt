package sa.sauditourism.employee.modules.activities.model

import com.google.gson.annotations.SerializedName

data class SearchUserResponse(
    @SerializedName("users")
    val users: List<User>
)

data class User(
    @SerializedName("key")
    val key: String,
    @SerializedName("displayName")
    val displayName: String,
    @SerializedName("name")
    val name: String,
)
