package sa.sauditourism.employee.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.coil.CoilImageState
import sa.sauditourism.employee.LocalLanguage
import sa.sauditourism.employee.R
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.clickableWithoutRipple
import sa.sauditourism.employee.shimmer.shimmerModifier
import sa.sauditourism.employee.ui.theme.AppColors
import sa.sauditourism.employee.ui.theme.AppFonts

@Composable
fun CheckBoxComponent(onCheckedChange: (FilterCheckboxState) -> Unit,
                      modifier: Modifier = Modifier,
                      title: String? = null,
                      subtitle: String? = null,
                      checked: FilterCheckboxState = FilterCheckboxState.Unchecked,
                      enabled: Boolean = true,
                      textColor: Color? = null,
                      displayImage:Boolean= false,
                      imageUrl:String? = null,


) {
    val checkState = remember(checked) {
        mutableStateOf(checked)
    }

    Row(
        modifier = modifier.clickableWithoutRipple {
            if (enabled) {
                val newValue = if (checkState.value == FilterCheckboxState.Unchecked || checkState.value == FilterCheckboxState.PartialChecked) {
                    FilterCheckboxState.Checked
                } else {
                    FilterCheckboxState.Unchecked
                }
                checkState.value = newValue
                onCheckedChange(newValue)
            }
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        CustomCheckbox(
                state = checkState.value,
                onStateChanged = { newValue ->
                    if (enabled) {
                        checkState.value = newValue
                        onCheckedChange(newValue)
                    }
                },
                enabled = enabled,
            )

        if(displayImage){
            Spacer(Modifier.width(12.dp))

            MediaComponent(
                modifier = Modifier.size(40.dp).shimmerModifier(isLoading = false),
                mediaContent = MediaContent(GalleryType.IMAGE, source = R.drawable.ic_avatar),
                isLoading = false,
                onImageStateChanged = {
                    if (it is CoilImageState.Success || it is CoilImageState.Failure) {  false }
                },
                placeHolder = R.drawable.ic_avatar
            )
            Spacer(Modifier.width(6.dp))
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 2.dp)
                .wrapContentSize()
        ) {
            title?.let {
                CustomTextComponent(
                    text = it,
                    style =
                    AppFonts.body2Regular,
                    color = textColor ?: MaterialTheme.colorScheme.inverseSurface,
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            subtitle?.let {
                CustomTextComponent(
                    text = it, style =
                    AppFonts.body2Regular,
                    color = AppColors.GreyColor71
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0x000000, widthDp = 250)
@Composable
fun CheckboxComponentPreview() {
    CompositionLocalProvider(LocalLanguage provides LanguageConstants.DEFAULT_LOCALE) {
        Column {
            CheckBoxComponent(onCheckedChange = {})
            CheckBoxComponent(title = "Checkbox", onCheckedChange = {})
            CheckBoxComponent(title = "Checkbox",
                subtitle = "Here goes a supporting text for the checkbox",
                onCheckedChange = {})

            CheckBoxComponent(onCheckedChange = {}, checked = FilterCheckboxState.Checked)
            CheckBoxComponent(
                title = "Checkbox",
                onCheckedChange = {},
                checked = FilterCheckboxState.Checked
            )
            CheckBoxComponent(
                title = "Checkbox",
                subtitle = "Here goes a supporting text for the checkbox",
                onCheckedChange = {}, checked = FilterCheckboxState.Checked
            )
            CheckBoxComponent(enabled = false, onCheckedChange = {})
            CheckBoxComponent(
                enabled = false,
                title = "Checkbox",
                onCheckedChange = {},
                checked = FilterCheckboxState.Checked
            )
            CheckBoxComponent(
                title = "Checkbox",
                subtitle = "Here goes a supporting text for the checkbox",
                onCheckedChange = {}, checked = FilterCheckboxState.Unchecked, enabled = false
            )
        }
    }
}