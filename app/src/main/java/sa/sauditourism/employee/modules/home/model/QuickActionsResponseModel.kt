package sa.sauditourism.employee.modules.home.model


import com.google.gson.annotations.SerializedName

data class QuickActionsResponseModel(
    @SerializedName("actions")
    val actions: List<Action>
)