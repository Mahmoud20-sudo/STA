package sa.sauditourism.employee.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sa.sauditourism.employee.extensions.clickableWithoutRipple
import sa.sauditourism.employee.modules.services.model.RequestsType
import sa.sauditourism.employee.modules.services.model.ServicesModel
import sa.sauditourism.employee.shimmer.shimmerModifier

data class FilterModel(
    val id: String,
    val title: String,
    var isExpanded: Boolean = false,
    val childList: List<FilterModel> = emptyList(),
    var isSelected: MutableState<FilterCheckboxState> = mutableStateOf(FilterCheckboxState.Unchecked)
)

@Composable
fun AnimateExpandableList(
    list: List<ServicesModel>,
    isLoading: Boolean = false,
    onCheckedChange: (String, RequestsType) -> Unit,
) {
    val items = remember { list.ifEmpty { List(3) { ServicesModel() } } }
    val expandedStates =
        remember { mutableStateListOf(*BooleanArray(items.size) { items[it].isSelected.value == FilterCheckboxState.Checked }.toTypedArray()) }

    items.onEachIndexed { index, item ->
        ExpandableListItem(
            item = item,
            index = index,
            isLoading = isLoading,
            isExpanded = expandedStates[index],
            onExpandedChange = {
                expandedStates[index] = it
            },
            clearSelections = {
                items.onEachIndexed { i, service ->
                    service.isSelected.value =
                        if (i == index) FilterCheckboxState.Checked else FilterCheckboxState.Unchecked
                    service.requestsTypes.onEach { requestsType ->
                        requestsType.isSelected.value = FilterCheckboxState.Unchecked
                    }
                    expandedStates[i] = i == index
                }
            },
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun ExpandableListItem(
    item: ServicesModel,
    index: Int,
    isLoading: Boolean = false,
    isExpanded: Boolean,
    clearSelections: () -> Unit,
    onExpandedChange: (Boolean) -> Unit,
    onCheckedChange: (String, RequestsType) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clickableWithoutRipple {
                onExpandedChange(!isExpanded)
            }
    ) {
        CheckBoxComponent(
            modifier = Modifier
                .fillMaxWidth()
                .shimmerModifier(isLoading),
            title = item.title,
            checked = item.isSelected.value,
            onCheckedChange = { _ ->
//                item.isSelected.value = newValue
                onExpandedChange(!isExpanded)
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            ) {
                item.requestsTypes.onEach { child ->
                    RadioButtonComponent(
                        modifier = Modifier.shimmerModifier(isLoading),
                        title = child.title,
                        onSelectedChange = { checked ->
                            clearSelections()
                            item.requestsTypes.onEach {
                                it.isSelected.value = FilterCheckboxState.Unchecked
                            }
                            child.isSelected.value =
                                if (checked) FilterCheckboxState.Checked else FilterCheckboxState.Unchecked

                            onCheckedChange(item.id, child)
                        },
                        selected = child.isSelected.value === FilterCheckboxState.Checked,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}