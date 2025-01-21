package sa.sauditourism.employee.modules.services.model.participants


import com.google.gson.annotations.SerializedName

data class ParticipantsResponseModel(
    @SerializedName("participants")
    val participants: List<Participant>
)