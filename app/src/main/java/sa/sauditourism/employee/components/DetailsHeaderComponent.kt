package sa.sauditourism.employee.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.compose.rememberNavController
import ir.kaaveh.sdpcompose.sdp
import sa.sauditourism.employee.R
import sa.sauditourism.employee.constants.LanguageConstants
import sa.sauditourism.employee.extensions.clickableWithoutRipple
import sa.sauditourism.employee.extensions.isNotNull
import sa.sauditourism.employee.extensions.localizeString
import sa.sauditourism.employee.extensions.mirror
import sa.sauditourism.employee.managers.sharedprefrences.SharedPreferencesManager
import sa.sauditourism.employee.resources.AppFonts
import sa.sauditourism.employee.ui.theme.AppColors
import androidx.compose.runtime.remember as remember1

@Composable
fun DetailsHeaderComponent(
    headerModel: HeaderModel,
    modifier: Modifier = Modifier,
    showOverlay: Boolean = true,
    showDarkTextIcons: Boolean = true,
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
    searchPlaceHolder:String?=LanguageConstants.SEARCH_FOR_REQUESTS.localizeString()
) {
    val transition = updateTransition(targetState = showOverlay, label = "BackgroundTransition")
    var showBottomShadow by remember { mutableStateOf(false) }

    val title = headerModel.title
    val subTitle = headerModel.subTitle

    val showBack = headerModel.showBackIcon
    val navController = rememberNavController()

    val background by transition.animateColor(
        transitionSpec = {
            if (targetState) {
                tween(durationMillis = if (showDarkTextIcons) 0 else 300)
            } else {
                tween(durationMillis = if (showDarkTextIcons) 0 else 300)
            }
        },
        label = "background",
    ) { isOverlay ->
        if (isOverlay) {
            showBottomShadow = false
            Color.Transparent
        } else {
            showBottomShadow = true
            MaterialTheme.colorScheme.background // Change this to your desired color when showOverlay is false
        }
    }

    val brush = remember1 {
        Brush.verticalGradient(
            colors = listOf(
                Color.Black.copy(alpha = 0.0f),
                Color.Transparent,
            ),
            startY = 0f,
            endY = Float.POSITIVE_INFINITY,
        )
    }

    val isOverlay = transition.targetState


    Crossfade(
        animationSpec = tween(durationMillis = if (showDarkTextIcons) 0 else 100),
        targetState = isOverlay,
        label = "cross-fade",
    ) { isFadeOverlay ->
        val shadowModifier = Modifier
            .fillMaxWidth()

        Column(
            modifier = shadowModifier
                .background(backgroundColor)
                .then(
                    if (isFadeOverlay) {
                        Modifier.background(brush)
                    } else {
                        Modifier.background(
                            background,
                        )
                    },
                )
                .wrapContentHeight()
                .then(modifier)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 16.dp)
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
                                        .border(
                                            1.4.dp,
                                            color = AppColors.White,
                                            shape = CircleShape
                                        )
                                        .clip(CircleShape)
                                        .padding(top = 2.sdp)
                                )
                        }
                    }
                }

                this@Column.AnimatedVisibility(
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


                    }
                }
            }

            headerBottomComponent?.invoke()

            //todo check with Mahmoud why was it inside the container
            if (headerModel.showSearch) {
                InputFieldComponent(
                    value = headerModel.searchValue.orEmpty(),
                    title = headerModel.searchTitle,
                    onValueChange = onSearchValueChange,
                    inputFieldModel = InputFieldModel(
                        placeholderText = searchPlaceHolder,
                        type = InputFieldType.Search,
                    ),
                    trailingView = searchTrailingView,
                    modifier = Modifier.padding(horizontal = 24.dp),
                )
                Spacer(modifier = Modifier.height(10.dp))

            }

            Spacer(modifier = Modifier.height(9.dp))

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
                            modifier = Modifier.padding(vertical = 16.dp),
                            itemState = ItemState.Active,
                        ) {
                            onTagClick.invoke(index)
                        }
                    }
                }
            }

            if (showBottomShadow)
                BottomShadow(0.15f, 2.dp)
        }
    }
}

@Composable
private fun BottomShadow(alpha: Float = 0.15f, height: Dp = 8.dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = alpha),
                        Color.Transparent,
                    )
                )
            )
    )
}
