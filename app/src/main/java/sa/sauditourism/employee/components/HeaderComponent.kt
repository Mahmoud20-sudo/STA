package sa.sauditourism.employee.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import sa.sauditourism.employee.LocalNavController
import sa.sauditourism.employee.R
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.isNotNull
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.extensions.mirror
import sa.sauditourism.employee.shimmer.shimmerModifier
import sa.sauditourism.employee.ui.theme.AppColors
import sa.sauditourism.employee.ui.theme.AppFonts

data class HeaderModel(
    val title: String? = null,
    val subTitle: String? = null,
    val showMapIcon: Boolean = false,
    val showShareIcon: Boolean = false,
    val showFilterIcon: Boolean = false,
    val showFavoriteIcon: Boolean = false,
    val showBackIcon: Boolean = true,
    val showSearch: Boolean = false,
    val searchValue: String? = null,
    val shareUrl: String? = null,
    val searchTitle: String? = null,
    val tags: List<String?>? = null,
    val filtersNumber: Int = 0,
    @DrawableRes val favoriteIconRes: Int = R.drawable.ic_favourite
)

@Composable
fun HeaderComponent(
    headerModel: HeaderModel,
    showTitle: Boolean = true,
    showSubtitle: Boolean = false,
    showTopBarShadow: Boolean = true,
    searchTrailingView: @Composable (() -> Unit)? = null,
    onSearchValueChange: (String) -> Unit = {},
    backHandler: (() -> Boolean?)? = null,
    filterClick: (() -> Unit)? = null,
    onTagClick: (position: Int) -> Unit = {},
    titleMaxLines: Int? = null,
    isStandAlone: Boolean = false,
    isLoading: Boolean = false,
    backgroundColor: Color = Color.White,
    showShadow: Boolean = true,
    headerBottomComponent: @Composable (() -> Unit)? = null,
) {
    val context = LocalContext.current

    val title = headerModel.title
    val subTitle = headerModel.subTitle

    val showBack = headerModel.showBackIcon
    val navController = rememberNavController()

    val modifier = if (showShadow) Modifier
        .shadow(
            if (showTopBarShadow) 6.dp else 0.dp,
            shape = MaterialTheme.shapes.small.copy(CornerSize(0.dp)),
        ) else Modifier

    Box(
        modifier = modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .then(if (isStandAlone) Modifier else Modifier.height(160.dp))
            .padding(
                top = if (!isStandAlone) 0.dp else 4.dp,
                bottom = if (!isStandAlone) 8.dp else 12.dp
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            if (showBack && !isLoading) {
                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                    IconButton(
                        modifier = Modifier
                            .size(35.dp)
                            .align(Alignment.CenterStart)
                            .mirror()
                            .padding(end = if (isLoading) 24.dp else 0.dp),
                        onClick = {
                            if (backHandler.isNotNull()) {
                                backHandler.invoke()
                            } else {
                                navController.popBackStack()
                            }
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_left_tail),
                            contentDescription = "Back",
                        )
                    }
                }
            }

            if (headerModel.showFavoriteIcon) {
                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                    IconButton(modifier = Modifier
                        .size(35.dp)
                        .align(Alignment.TopEnd), onClick = {
                    }) {
                        Icon(
                            painter = painterResource(id = headerModel.favoriteIconRes),
                            contentDescription = "Favorite",
                            tint = Color.Unspecified
                        )
                    }
                }
            }

            if (headerModel.showFilterIcon) {
                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
                    Box(
                        modifier = Modifier
                            .size(35.dp)
                            .align(Alignment.TopEnd)
                    ) {
                        IconButton(modifier = Modifier.fillMaxSize(), onClick = {
                            filterClick?.invoke()
                        }) {
                            Icon(
                                painter = painterResource(id = headerModel.favoriteIconRes),
                                contentDescription = "Favorite",
                                tint = Color.Unspecified
                            )
                        }

                        if (headerModel.filtersNumber > 0)
                            CustomTextComponent(
                                text = headerModel.filtersNumber.toString(),
                                color = AppColors.White,
                                textAlign = TextAlign.Center,
                                style = AppFonts.capsSmallSemiBold,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .align(Alignment.TopEnd)
                                    .size(17.dp)
                                    .background(
                                        color = AppColors.OnPrimaryColor,
                                        shape = CircleShape
                                    )
                                    .border(1.4.dp, color = AppColors.White, shape = CircleShape)
                                    .clip(CircleShape)
                                    .padding(top = 3.dp)
                            )
                    }
                }
            }
        }

        AnimatedVisibility(
            showTitle && !isLoading,
            enter = fadeIn(animationSpec = tween(500, easing = LinearEasing)),
            exit = fadeOut(animationSpec = tween(500, easing = LinearEasing)),
            modifier = Modifier.align(Alignment.TopCenter),
        ) {
            Column(Modifier) {
                CustomTextComponent(
                    modifier = Modifier
                        .padding(start = if (showBack) 45.dp else 10.dp, end = 35.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    text = title.orEmpty(),
                    overflow = if (titleMaxLines.isNotNull()) TextOverflow.Clip else TextOverflow.Ellipsis,
                    maxLines = if (titleMaxLines.isNotNull()) titleMaxLines else 1,
                    textAlign = TextAlign.Center,
                    style = AppFonts.headerMedium,
                )
                if (showSubtitle) {
                    Spacer(modifier = Modifier.height(14.dp))
                    CustomTextComponent(
                        text = subTitle.orEmpty(),
                        color = AppColors.ColorsPrimitivesTwo,
                        style = AppFonts.body2Regular,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(start = 35.dp, end = 35.dp)
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                    )
                }
                if (headerModel.showSearch) {
                    Spacer(modifier = Modifier.height(24.dp))
                    InputFieldComponent(
                        value = headerModel.searchValue.orEmpty(),
                        title = headerModel.searchTitle,
                        onValueChange = onSearchValueChange,
                        inputFieldModel = InputFieldModel(
                            placeholderText = LanguageConstants.SEARCH_FOR_REQUESTS.localizeString(),
                            type = InputFieldType.Search,
                        ),
                        trailingView = searchTrailingView,
                        modifier = Modifier.padding(horizontal = 24.dp),
                    )
                }

                if (!headerModel.tags.isNullOrEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp),
                    ) {
                        this.itemsIndexed(headerModel.tags) { index, tag ->
                            if (tag.isNullOrEmpty()) return@itemsIndexed
                            CategoryTagComponent(
                                text = tag,
                                icon = R.drawable.ic_close,
                                withEndIcon = true,
                                itemSize = ItemSize.Large,
                                isLoading = isLoading,
                                modifier = Modifier.padding(top = 16.dp),
                                itemState = ItemState.Active,
                            ) {
                                onTagClick.invoke(index)
                            }
                        }
                    }
                }

                headerBottomComponent?.invoke()
            }
        }
    }
}
