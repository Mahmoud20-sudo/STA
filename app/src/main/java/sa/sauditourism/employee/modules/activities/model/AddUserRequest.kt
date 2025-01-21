package sa.sauditourism.employee.modules.activities.model

import com.google.gson.annotations.SerializedName

data class AddUsersRequest(
    @SerializedName("usernames")
    val usersName:List<String>,
    val action:String = "add"
)
