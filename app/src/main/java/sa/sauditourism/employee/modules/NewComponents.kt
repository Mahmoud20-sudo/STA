package sa.sauditourism.employee.modules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sa.sauditourism.employee.components.InputFieldComponent
import sa.sauditourism.employee.components.InputFieldModel
import sa.sauditourism.employee.components.InputFieldType

@Composable
fun NewComponents() {
    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

            InputFieldComponent(
                title = "Search with error",
                value = "",
                inputFieldModel = InputFieldModel(
                    placeholderText = "Search Here",
                    type = InputFieldType.Search,
                ),
                errorMessage = "Error",
                isOptional = true,
                onValueChange = {},
            )


            InputFieldComponent(
                title = "Normal",
                value = "",
                inputFieldModel = InputFieldModel(
                    placeholderText = "Enter value",
                    type = InputFieldType.Normal,
                    showCharCounter = true,
                    maxLines = 3,
                    minLines = 3,
                    singleLine = false,
                    maxCharLimit = 120,
                ),
                isOptional = true,
                onValueChange = {},
            )



            InputFieldComponent(
                title = "Normal with info",
                value = "",
                inputFieldModel = InputFieldModel(
                    placeholderText = "Enter value",
                    type = InputFieldType.Normal,
                ),
                onValueChange = {},
                informativeMessage = "Info",
            )


            InputFieldComponent(
                title = "Numeric",
                value = "",
                inputFieldModel = InputFieldModel(
                    placeholderText = "Enter value",
                    type = InputFieldType.Numbers
                ),
                onValueChange = {},
            )
        }


    }
