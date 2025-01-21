package sa.sauditourism.employee.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import sa.sauditourism.employee.LocalLanguage
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.resources.AppColors.OnPrimaryDark
import sa.sauditourism.employee.ui.theme.AppColors
import sa.sauditourism.employee.ui.theme.AppFonts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RadioButtonComponent(
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
        .padding(horizontal = 8.dp, vertical = 2.dp),
    title: String? = null,
    subtitle: String? = null,
    selected: Boolean = false,
    enabled: Boolean = true,
) {
    var selectValue = selected
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier.selectable(selected = selectValue, onClick = {
            if (enabled) {
                selectValue = !selectValue
                onSelectedChange(selectValue)
            }
        }),
    ) {

        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
            RadioButton(
                selected = selectValue,
                onClick = {
                    if (enabled) {
                        selectValue = !selectValue
                        onSelectedChange(selectValue)
                    }
                },

                enabled = enabled,
                colors = RadioButtonDefaults.colors(
                    selectedColor = OnPrimaryDark,
                    unselectedColor = AppColors.ColorsPrimitivesThree,
                    disabledSelectedColor = AppColors.ColorsGrayA,
                    disabledUnselectedColor = AppColors.ColorsGrayA
                ),
            )

            if (!title.isNullOrEmpty() || !subtitle.isNullOrEmpty()) {
                Column(
                    modifier = modifier
                        .padding(start = 5.dp)
                        .wrapContentSize(),
                ) {
                    title?.let {
                        CustomTextComponent(
                            text = it,
                            style =
                            AppFonts.body2Regular,
                            color = if (enabled) MaterialTheme.colorScheme.inverseSurface else AppColors.ColorsGrayA,
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    subtitle?.let {
                        CustomTextComponent(
                            text = it,
                            style =
                            AppFonts.body2Regular,
                            color = if (enabled) AppColors.GreyColor71 else AppColors.ColorsGrayA,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun RadioButtonComponentPreview() {
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
                enabled = true,
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