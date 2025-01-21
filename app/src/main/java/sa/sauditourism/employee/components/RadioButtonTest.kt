package sa.sauditourism.employee.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sa.sauditourism.employee.LocalLanguage
import sa.sauditourism.employee.constants.LanguageConstants

@Composable
fun RadioButtonTest() {
    val radioOptions = listOf("First", "Second", "Third")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[1]) }
    CompositionLocalProvider(LocalLanguage provides LanguageConstants.DEFAULT_LOCALE) {
        Column {
            radioOptions.forEach { txtVal ->
                RadioButtonComponent(
                    title = txtVal,
                    selected = txtVal == selectedOption,
                    onSelectedChange = { onOptionSelected(txtVal) },
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            RadioButtonComponent(
                enabled = false,
                title = "RadioButton",
                onSelectedChange = {},
                selected = true,
            )
            RadioButtonComponent(
                title = "RadioButton",
                subtitle = "Here goes a supporting text for the RadioButton",
                onSelectedChange = {},
                selected = false,
                enabled = false,
            )
        }
    }
}