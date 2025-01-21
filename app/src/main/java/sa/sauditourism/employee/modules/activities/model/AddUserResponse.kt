package sa.sauditourism.employee.modules.activities.model

import com.google.gson.annotations.SerializedName
import sa.sauditourism.employee.modules.services.model.participants.Participant

data class AddUserResponse(
    @SerializedName("participants")
    val participants: List<Participant>,
)
