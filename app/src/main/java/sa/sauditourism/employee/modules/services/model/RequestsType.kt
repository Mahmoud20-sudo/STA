package sa.sauditourism.employee.modules.services.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.encoders.annotations.Encodable.Ignore
import com.google.gson.annotations.SerializedName
import sa.sauditourism.employee.components.FilterCheckboxState

@Immutable
data class RequestsType(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("serviceId")
    val serviceId: String = "",
    @SerializedName("issueId")
    val issueId: String = "",
    @SerializedName("image")
    val image: String = "",
    @SerializedName("subTitle")
    val subTitle: String? = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("note")
    val note: String = "",
    @SerializedName("submissionRequestType")
    var submissionRequestType: String="",
    @SerializedName("commingSoon")
    var comingSoon: Boolean = false
){
    @Transient
    var isSelected: MutableState<FilterCheckboxState> = mutableStateOf(FilterCheckboxState.Unchecked)
}
