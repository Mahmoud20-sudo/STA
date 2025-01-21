package sa.sauditourism.employee.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import sa.sauditourism.employee.LocalNavController
import sa.sauditourism.employee.R
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.clickableWithoutRipple
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.extensions.mirror
import sa.sauditourism.employee.resources.AppFonts
import sa.sauditourism.employee.shimmer.shimmerModifier

data class SearchHeaderComponentModel(
    val title: String?,
    var searchValue: String? = null,
    val tags: List<String>? = null,
    val showBack: Boolean = false,
    val searchHeaderType: SearchHeaderType = SearchHeaderType.BIG,
)

enum class SearchHeaderType {
    BIG, SMALL
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchHeaderComponent(
    searchHeaderModel: SearchHeaderComponentModel,
    modifier: Modifier = Modifier,
    loading: MutableState<Boolean> = mutableStateOf(false),
    clickable: Boolean = false,
    onSearchValueChange: (String) -> Unit = {},
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    onTagClick: (position: Int) -> Unit = {},
    onSearchClick: () -> Unit = {},
    onStartSearch: (() -> Unit)? = null,
    onFocusChange: ((Boolean) -> Unit)? = null
) {
    val navController = rememberNavController()
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager: FocusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                0.dp,
                shape = MaterialTheme.shapes.small.copy(CornerSize(0.dp)),
            ) // Add a bottom shadow with 10dp elevation
            .background(backgroundColor),
    ) {
        if (searchHeaderModel.searchHeaderType == SearchHeaderType.BIG) {
            CustomTextComponent(
                text = searchHeaderModel.title.orEmpty(),
                isLoading = loading.value,
                modifier = Modifier
                    .padding(horizontal = 24.dp),
                style = AppFonts.headerBold,
                lineHeight = 54.sp,
            )
            Spacer(modifier = Modifier.height(if(searchHeaderModel.title.isNullOrEmpty()) 0.dp else 30.dp))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (searchHeaderModel.showBack) {
                Spacer(modifier = Modifier.width(16.dp))
                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides  Dp.Unspecified) {
                    IconButton(
                        modifier = Modifier
                            .size(35.dp)
                            .mirror(),
                        onClick = {
                            localFocusManager.clearFocus()
                            keyboardController?.hide()
                            onSearchValueChange.invoke("")
                            onFocusChange?.invoke(false) ?: navController.popBackStack()
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_left_tail),
                            contentDescription = "Back",
                        )
                    }
                }
            }
            InputFieldComponent(
                value = searchHeaderModel.searchValue.orEmpty(),
                onValueChange = onSearchValueChange,
                clickable = clickable,
                onStartSearch = onStartSearch,
                onFocusChange = onFocusChange,
                keyboardController = keyboardController,
                inputFieldModel = InputFieldModel(
                    placeholderText = LanguageConstants.SEARCH_FOR_REQUESTS.localizeString(),
                    type = InputFieldType.Search,
                ),
                modifier = Modifier
                    .padding(
                        start = if (searchHeaderModel.searchHeaderType == SearchHeaderType.SMALL) 16.dp else 24.dp,
                        end = 24.dp,
                    )
                    .shimmerModifier(loading.value)
                    .clickableWithoutRipple { onSearchClick.invoke() },
            )
        }
        if (!searchHeaderModel.tags.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))
            val birthdayIcon =
                "https://drive.google.com/uc?export=view&id=1pAFSt1eo2tA5kmhcT5FcVAvVlb4ah-f6"
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 24.dp),
            ) {
                this.itemsIndexed(searchHeaderModel.tags) { index, tag ->
                    CategoryTagComponent(
                        text = tag,
                        icon = birthdayIcon,
                        withEndIcon = true,
                        itemSize = ItemSize.Large,
                        isLoading = loading.value,
                        itemState = ItemState.Active,
                    ) {
                        onTagClick.invoke(index)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(35.dp))
    }
}
