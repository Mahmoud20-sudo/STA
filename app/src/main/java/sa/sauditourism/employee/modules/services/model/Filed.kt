package sa.sauditourism.employee.modules.services.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import sa.sauditourism.employee.components.DropDownModel
import sa.sauditourism.employee.constants.FirebaseConstants.DATE
import sa.sauditourism.employee.constants.FirebaseConstants.DATE_AND_TIME
import sa.sauditourism.employee.constants.FirebaseConstants.DROPDOWN_MULTI_SELECT
import sa.sauditourism.employee.constants.FirebaseConstants.DROPDOWN_SINGLE_SELECT
import sa.sauditourism.employee.constants.FirebaseConstants.MULTI_DROPDOWNS
import sa.sauditourism.employee.constants.FirebaseConstants.TIME

data class Filed(
    @SerializedName("component")
    val component: String,
    @SerializedName("dropDownCount")
    val dropDownCount: Int,
    @SerializedName("id")
    val id: String,
    @SerializedName("isDropDownUserSearch")
    val isDropDownUserSearch: Boolean,
    @SerializedName("note")
    val note: String,
    @SerializedName("required")
    val required: Boolean,
    @SerializedName("title")
    val title: String,

    @Transient
    var dropDownValues: SnapshotStateList<DropDownModel>? = null,

    @Transient
    var showPicker: MutableState<Boolean>? = null

) {
    fun initPickers() {
        showPicker =
            if (component == DATE || component == TIME || component == DATE_AND_TIME) mutableStateOf(false) else null
    }

    fun initDropDowns(){
        dropDownValues =
            if (component == DROPDOWN_SINGLE_SELECT || component == DROPDOWN_MULTI_SELECT || component == MULTI_DROPDOWNS) mutableStateListOf() else null
    }
}