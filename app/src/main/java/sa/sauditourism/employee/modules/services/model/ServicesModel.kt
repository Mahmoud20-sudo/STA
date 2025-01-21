package sa.sauditourism.employee.modules.services.model


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.gson.annotations.SerializedName
import sa.sauditourism.employee.components.FilterCheckboxState

data class ServicesModel(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("image")
    val image: String = "",
    @SerializedName("requestsTypes")
    val requestsTypes: List<RequestsType> = emptyList(),
    @SerializedName("title")
    val title: String = "",
    @SerializedName("commingSoon")
    val comingSoon: Boolean = false
){
    @Transient
    var isSelected: MutableState<FilterCheckboxState> = mutableStateOf(FilterCheckboxState.Unchecked)
}