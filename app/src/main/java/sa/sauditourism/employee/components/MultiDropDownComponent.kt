package sa.sauditourism.employee.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.constants.LanguageConstants.NONE
import sa.sauditourism.employee.extensions.localizeString
import timber.log.Timber

@Composable
fun MultiDropDownComponent(
    title: String? = null,
    informativeMessage: String? = null,
    isOptional: Boolean = false,
    readOnly: Boolean = false,
    modifier: Modifier = Modifier,
    isExpanded: MutableState<Boolean?> = mutableStateOf(null),
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    focusManager: FocusManager = LocalFocusManager.current,
    onSearchInvoked: (String) -> Unit = {},
    onFocusChanged: (Boolean) -> Unit = {},
    onDropDownItemSelected: ((Any?, Any?) -> Unit) = { _, _ -> },
    numberOfComponents: Int = 0,
    list: List<DropDownModel> = emptyList()
) {
    var selectedIndex by remember { mutableIntStateOf(0) }
//    val selectedItems = remember { mutableStateListOf<Any>() }
//
//    LaunchedEffect(true) {
//        runCatching { selectedItems.addAll(list[1].innerList) }
//    }

    Column(modifier = modifier) {
        if (!readOnly)
            (0..<numberOfComponents).forEachIndexed { index, parentItem ->

                InputFieldComponent(
                    value = if (readOnly) list[index].label else "",
                    modifier = Modifier.onFocusChanged {
                        onFocusChanged.invoke(it.isFocused)
                    },
                    inputFieldModel = InputFieldModel(
                        placeholderText = LanguageConstants.SEARCH.localizeString(),
                        type = InputFieldType.Dropdown,
                        dropdownWithSearchIcon = true,
                    ),
                    originalModels = if (index == 0) list else kotlin.runCatching {
                        list.find { it.selected }!!.innerList
                    }.getOrDefault(emptyList()),
                    dropDownType = DropDownType.SingleSelection,
                    readOnly = readOnly,
                    title = if (index == 0) title else null,
                    isOptional = if (index == 0) isOptional else false,
                    informativeMessage = if (index == numberOfComponents - 1) informativeMessage else null,
                    enabled = if (index == 0) !readOnly else index <= selectedIndex,
                    isMultiDropDown = true,
                    isExpanded = isExpanded,
                    localFocusManager = focusManager,
                    keyboardController = keyboardController,
                    onDropDownItemSelected = { innerItem, previousId ->
                        //item select / reset
                        val itemId = (innerItem as DropDownModel).stringId
                        selectedIndex =
                            if (itemId == null) index else index + 1

                        Timber.d("Selected Index $selectedIndex")

                        if (list.indexOf(innerItem) > -1) {
                            //reset parent item selections
                            list.forEach { it.selected = false }
                            list[list.indexOf(innerItem)].selected = true
                        }
                        onDropDownItemSelected.invoke(innerItem, previousId)
                    },
                    onValueChange = {
                        if (index == 0 && list.isEmpty())
                            onSearchInvoked.invoke(it)
                    },
                    selectedDropDownItem = kotlin.runCatching {
                        if (index > selectedIndex)
                            DropDownModel(id = -1, label = NONE.localizeString())
                        else
                            selectedIndex
                    }.getOrNull()
                )
            }
        else
            InputFieldComponent(
                value = "",
                modifier = Modifier.onFocusChanged {
                    onFocusChanged.invoke(it.isFocused)
                },
                inputFieldModel = InputFieldModel(
                    placeholderText = LanguageConstants.SEARCH.localizeString(),
                    type = InputFieldType.Dropdown,
                    dropdownWithSearchIcon = true,
                ),
                originalModels = list,
                dropDownType = DropDownType.SingleSelection,
                readOnly = readOnly,
                title = null,
                isOptional = false,
                informativeMessage = null,
                enabled = false,
                isMultiDropDown = true,
                isExpanded = isExpanded,
                localFocusManager = focusManager,
                keyboardController = keyboardController,
                onDropDownItemSelected = { innerItem, previousId -> },
                onValueChange = {},
                selectedDropDownItem = {}
            )
    }
}