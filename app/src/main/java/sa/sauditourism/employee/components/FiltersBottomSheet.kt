package sa.sauditourism.employee.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sa.sauditourism.employee.EmployeeApplication.Companion.sharedPreferencesManager
import sa.sauditourism.employee.R
import sa.sauditourism.employee.constants.LanguageConstants.DONE_BUTTON_TITLE
import sa.sauditourism.employee.extensions.clickableOnce
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.managers.language.model.SupportedLanguage
import sa.sauditourism.employee.resources.AppFonts
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sa.sauditourism.employee.resources.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersBottomSheet(
    modifier: Modifier = Modifier,
    title: String? = null,
    font : TextStyle = AppFonts.subtitle1SemiBold,
    originalModels: ArrayList<SupportedLanguage>,
    onCloseClick: () -> Unit = {},
    onDismiss: (String) -> Unit = {},
) {
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    // get preferred language from shared preferences
    var locale = sharedPreferencesManager.preferredLocale
    if (locale.isNullOrEmpty()) {
        locale = Locale.current.language
    }

    var selectedLang by remember { mutableStateOf(originalModels.first { it.code == locale }.code) }

    ModalBottomSheet(
        onDismissRequest = { onCloseClick() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = AppColors.WHITE,
        modifier = modifier
    ) {
        title?.let {

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomTextComponent(
                    text = title,
                    modifier = Modifier.weight(1f),
                    style = font,
                    lineHeight = 29.sp,
                    color = AppColors.BLACK
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    "close",
                    tint = Color.Unspecified,
                    modifier = Modifier.clickableOnce {
                        scope.launch {
                            delay(100)
                            modalBottomSheetState.hide()
                            onCloseClick.invoke()
                        }
                    }
                )
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(), content = {
            itemsIndexed(originalModels) { index, item ->
                LanguageItemComponent(
                    languageItem = item,
                    shouldIncludeDivider = index < originalModels.size - 1,
                    selected = selectedLang == originalModels[index].code,
                    onItemClick = {
                        selectedLang = item.code
                    }
                )
            }
        })

        ButtonComponent(
            DONE_BUTTON_TITLE.localizeString(),
            clickListener = {
                scope.launch {
                    modalBottomSheetState.hide()
                    delay(100)
                    onDismiss.invoke(selectedLang)
                }
            },
            modifier = Modifier
                .padding(vertical = 22.dp, horizontal = 16.dp)
                .fillMaxWidth()
                .height(48.dp),
            buttonType = ButtonType.PRIMARY,
            buttonSize = ButtonSize.LARGE
        )
    }
}
