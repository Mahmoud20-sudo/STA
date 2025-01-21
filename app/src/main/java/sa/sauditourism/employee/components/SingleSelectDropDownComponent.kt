package sa.sauditourism.employee.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
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
import sa.sauditourism.employee.extensions.localizeString

@Composable
fun SingleSelectDropDownComponent(
    title: String? = null,
    informativeMessage: String? = null,
    isOptional: Boolean = false,
    isDropDownUserSearch: Boolean = false,
    modifier: Modifier = Modifier,
    defaultValue: String = "",
    readOnly: Boolean = false,
    isExpanded: MutableState<Boolean?> = mutableStateOf(null),
    keyboardController: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    focusManager: FocusManager = LocalFocusManager.current,
    onFocusChanged: (Boolean) -> Unit = {},
    onSearchInvoked: (String) -> Unit = {},
    onDropDownItemSelected: ((Any?) -> Unit) = {},
    list: List<DropDownModel> = emptyList()
) {
    var searchQuery by remember { mutableStateOf("") }

    Box(modifier = modifier.fillMaxSize()) {
        InputFieldComponent(
            value = defaultValue,
            modifier = Modifier.onFocusChanged {
                onFocusChanged.invoke(it.isFocused)
                if(it.isFocused)
                    onSearchInvoked.invoke(searchQuery)
            },
            inputFieldModel = InputFieldModel(
                placeholderText = LanguageConstants.SEARCH_HERE.localizeString(),
                type = InputFieldType.Dropdown,
                dropdownWithSearchIcon = true,
            ),
            title = title,
            isOptional = isOptional,
            originalModels = list,
            informativeMessage = informativeMessage,
            dropDownType = DropDownType.SingleSelection,
            readOnly = readOnly,
            enabled = !readOnly,
            isDropDownUserSearch = isDropDownUserSearch,
            isExpanded = isExpanded,
            localFocusManager = focusManager,
            keyboardController = keyboardController,
            onDropDownItemSelected = { item,_ ->
                onDropDownItemSelected.invoke(item)
            },
            onValueChange = {
                searchQuery = it
                onSearchInvoked.invoke(it)
            },
            selectedDropDownItem = DropDownModel(id = -1, stringId = null, label = "None")
        )
    }
}