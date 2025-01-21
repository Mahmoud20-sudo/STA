package sa.sauditourism.employee.modules

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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

val innerList1 = mutableListOf(
    DropDownModel(id = -1, label = LanguageConstants.NONE.localizeString()),
    DropDownModel(id = 1, label = "Inner 1"),
    DropDownModel(id = 2, label = "Inner 2"),
    DropDownModel(id = 3, label = "Inner 3")
)

val innerList2 = mutableListOf(
    DropDownModel(id = -1, label = LanguageConstants.NONE.localizeString()),
    DropDownModel(id = 1, label = "Inner 5"),
    DropDownModel(id = 2, label = "Inner 6"),
    DropDownModel(id = 3, label = "Inner 7")
)

val innerList3 = mutableListOf(
    DropDownModel(id = -1, label = LanguageConstants.NONE.localizeString()),
    DropDownModel(id = 1, label = "Inner 8"),
    DropDownModel(id = 2, label = "Inner 9"),
    DropDownModel(id = 3, label = "Inner 10")
)

val innerList4 = mutableListOf(
    DropDownModel(id = -1, label = LanguageConstants.NONE.localizeString()),
    DropDownModel(id = 1, label = "Inner 11"),
    DropDownModel(id = 2, label = "Inner 12"),
    DropDownModel(id = 3, label = "Inner 13")
)

@SuppressLint("MutableCollectionMutableState")
@Composable
fun TestMultiDropDowns(numberOfComponents: Int) {

    val originalModels = mutableListOf(
//        DropDownModel(id = -1, title = "None", innerList = emptyList()),
        DropDownModel(id = 1, label = "Attractions", innerList = innerList1),
        DropDownModel(id = 2, label = "Events", innerList = innerList2),
        DropDownModel(id = 3, label = "Articles", innerList = innerList3),
        DropDownModel(id = 3, label = "Blogs", innerList = innerList4)
    )

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val expanded = remember { mutableStateOf<Boolean?>(null) }
    val scope = rememberCoroutineScope()

    var selectedIndex by remember { mutableIntStateOf(0) }
    val selectedItems = remember { mutableStateListOf<DropDownModel>() }

    LaunchedEffect(true) {
        selectedItems.addAll(originalModels[1].innerList)
    }


    Column(modifier = Modifier
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
        (0..numberOfComponents).forEachIndexed { index, parentItem ->

            InputFieldComponent(
                value = "",
                inputFieldModel = InputFieldModel(
                    placeholderText = LanguageConstants.SEARCH.localizeString(),
                    type = InputFieldType.Dropdown,
                    dropdownWithSearchIcon = true,
                ),
                originalModels = originalModels[index].innerList,
                dropDownType = DropDownType.SingleSelection,
                readOnly = false,
                title = if (index == 0) LanguageConstants.SERVICES_TAB.localizeString() else "",
                isOptional = index == 0,
                informativeMessage = if(index == numberOfComponents) LanguageConstants.SERVICES_CATEGORIES.localizeString() else null,
                enabled = if (index == 0) true else index <= selectedIndex,
                isExpanded = expanded,
                localFocusManager = focusManager,
                keyboardController = keyboardController,
                onDropDownItemSelected = { innerItem,_ ->
                    //todo handle on item selected
                    //item select / reset
                    selectedIndex = if ((innerItem as DropDownModel).id == -1) index else index + 1
                },
                onValueChange = {},
                selectedDropDownItem = if (index == 0 || index > selectedIndex)
                    originalModels[index].innerList[0]
                else
                    parentItem
            )
        }
    }

}

