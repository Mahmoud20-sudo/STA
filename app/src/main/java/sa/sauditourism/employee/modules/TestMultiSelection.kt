package sa.sauditourism.employee.modules

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sa.sauditourism.employee.components.DropDownModel
import sa.sauditourism.employee.components.DropDownType
import sa.sauditourism.employee.components.InputFieldComponent
import sa.sauditourism.employee.components.InputFieldModel
import sa.sauditourism.employee.components.InputFieldType
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.clickableWithoutRipple
import sa.sauditourism.employee.extensions.localizeString

@SuppressLint("MutableCollectionMutableState")
@Composable
fun TestMultiSelection() {

    val list = mutableListOf(
        DropDownModel(id = 1, label = "Attractions"),
        DropDownModel(id = 2, label = "Events"),
        DropDownModel(id = 3, label = "Articles"),
        DropDownModel(id = 4, label = "Blogs")
    )

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val expanded = remember { mutableStateOf<Boolean?>(null) }
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 24.dp, vertical = 16.dp)
        .clickableWithoutRipple {
            scope.launch {
                expanded.value = false
                focusManager.clearFocus()
                keyboardController?.hide()
                delay(300)
                expanded.value = null
            }
        }
    ) {
        InputFieldComponent(
            value = "",
            title = LanguageConstants.SEARCH.localizeString(),
            informativeMessage = LanguageConstants.NOTE.localizeString(),
            inputFieldModel = InputFieldModel(
                placeholderText = LanguageConstants.SEARCH.localizeString(),
                type = InputFieldType.Dropdown,
                dropdownWithSearchIcon = true,

            ),
            originalModels = list,
            dropDownType = DropDownType.MultiSelection,
            readOnly = false,
            isExpanded = expanded,
            localFocusManager = focusManager,
            keyboardController = keyboardController,
            onDropDownItemSelected = { item,_ ->
                //todo handle on item selected
            },
            onValueChange = {},
            selectedDropDownItem = DropDownModel(id = -1, label = "None")
        )
    }

}

